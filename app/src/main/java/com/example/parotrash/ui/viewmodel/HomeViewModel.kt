package com.example.parotrash.ui.viewmodel

import android.app.Application
import android.location.Location
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.parotrash.data.LocationManager
import com.example.parotrash.data.NotificationHelper
import com.example.parotrash.data.NotificationPreferences
import com.example.parotrash.data.PermissionPreferences
import com.example.parotrash.data.SessionManager
import com.example.parotrash.modelos.Reporte
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class HomeViewModel(application: Application) : AndroidViewModel(application) {

    private val locationManager = LocationManager(application)
    private val permissionPreferences = PermissionPreferences(application)
    private val notificationPreferences = NotificationPreferences(application)
    private val notificationHelper = NotificationHelper(application)
    private val auth = FirebaseAuth.getInstance()
    private val firestore = FirebaseFirestore.getInstance()

    private val RADIO_NOTIFICACION_METROS = 100f // Radio de 100 metros, cambiar aquí si se quiere

    private val _ubicacion = MutableStateFlow<Location?>(null)
    val ubicacion: StateFlow<Location?> = _ubicacion

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _permisoConcedido = MutableStateFlow(locationManager.hasLocationPermission())
    val permisoConcedido: StateFlow<Boolean> = _permisoConcedido

    private val _reportes = MutableStateFlow<List<Reporte>>(emptyList())
    val reportes: StateFlow<List<Reporte>> = _reportes

    private var ultimosIdsProcesados = mutableSetOf<String>()

    var descripcion by mutableStateOf("")
        private set

    var tipo by mutableStateOf("")
        private set

    var cargandoReporte by mutableStateOf(false)
        private set

    var reporteExito by mutableStateOf(false)
        private set

    var errorReporte by mutableStateOf<String?>(null)
        private set

    init {
        escucharReportes()
        cargarUbicacionGuardada()
    }

    private fun cargarUbicacionGuardada() {
        viewModelScope.launch {
            val lat = permissionPreferences.lastLatitude.first()
            val lon = permissionPreferences.lastLongitude.first()
            if (lat != null && lon != null) {
                val savedLocation = Location("saved").apply {
                    latitude = lat
                    longitude = lon
                }
                _ubicacion.value = savedLocation
                Log.d("HomeViewModel", "Ubicación guardada cargada: $lat, $lon")
            }
        }
    }

    private fun escucharReportes() {
        firestore.collection("reportes")
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    Log.e("HomeViewModel", "Error en SnapshotListener: ${error.message}")
                    return@addSnapshotListener
                }

                if (snapshot != null) {
                    val listaValida = mutableListOf<Reporte>()
                    for (doc in snapshot.documents) {
                        try {
                            val reporte = doc.toObject(Reporte::class.java)?.copy(id = doc.id)
                            if (reporte != null) {
                                listaValida.add(reporte)

                                // Verificar si es un nuevo reporte para mostrar notificación
                                if (!ultimosIdsProcesados.contains(reporte.id)) {
                                    ultimosIdsProcesados.add(reporte.id)
                                    verificarYNotificarNuevoReporte(reporte)
                                }
                            }
                        } catch (e: Exception) {
                            Log.e("HomeViewModel", "Error al procesar reporte ${doc.id}: ${e.message}")
                        }
                    }
                    _reportes.value = listaValida
                }
            }
    }

    private fun verificarYNotificarNuevoReporte(reporte: Reporte) {
        viewModelScope.launch {
            try {
                // Obtener el UID del usuario actual
                val uidActual = auth.currentUser?.uid

                // No notificar si el reporte es del propio usuario
                if (reporte.id_usuario == uidActual) {
                    return@launch
                }

                // Verificar si el usuario tiene notificaciones habilitadas
                val recibirAlertas = notificationPreferences.recibirAlertas.first()
                if (!recibirAlertas) {
                    return@launch
                }

                // Obtener la ubicación actual del usuario
                val ubicacionActual = _ubicacion.value ?: return@launch

                // Verificar si el tipo de reporte está habilitado
                val tipoHabilitado = when (reporte.tipo) {
                    "Alerta" -> notificationPreferences.comunidad.first()
                    "Bus Varado", "Accidente" -> notificationPreferences.bloqueosViales.first()
                    "Manifestación" -> notificationPreferences.manifestaciones.first()
                    else -> false
                }

                if (!tipoHabilitado) {
                    return@launch
                }

                // Calcular distancia entre el reporte y el usuario
                val distancia = calcularDistancia(
                    ubicacionActual.latitude, ubicacionActual.longitude,
                    reporte.ubicacion[0], reporte.ubicacion[1]
                )

                // Si está dentro del radio, mostrar notificación
                if (distancia <= RADIO_NOTIFICACION_METROS) {
                    notificationHelper.mostrarNotificacionAlerta(
                        tipo = reporte.tipo,
                        descripcion = reporte.descripcion
                    )
                    Log.d("HomeViewModel", "Notificación enviada para: ${reporte.tipo} a ${distancia}m")
                }

            } catch (e: Exception) {
                Log.e("HomeViewModel", "Error al verificar notificación: ${e.message}")
            }
        }
    }

    private fun calcularDistancia(lat1: Double, lon1: Double, lat2: Double, lon2: Double): Float {
        val results = FloatArray(1)
        Location.distanceBetween(lat1, lon1, lat2, lon2, results)
        return results[0]
    }

    fun actualizarPermisos() {
        _permisoConcedido.value = locationManager.hasLocationPermission()
    }

    fun obtenerUbicacion() {
        viewModelScope.launch {
            actualizarPermisos()
            if (!_permisoConcedido.value) return@launch

            _isLoading.value = true
            try {
                var location = locationManager.getCurrentLocation()

                if (location == null) {
                    delay(1500)
                    location = locationManager.getCurrentLocation()
                }

                if (location != null) {
                    _ubicacion.value = location
                    permissionPreferences.setLastLocation(location.latitude, location.longitude)
                    Log.d("HomeViewModel", "Nueva ubicación obtenida y guardada: ${location.latitude}, ${location.longitude}")
                } else {
                    val lastLocation = locationManager.getLastKnownLocation()
                    _ubicacion.value = lastLocation
                    lastLocation?.let {
                        permissionPreferences.setLastLocation(it.latitude, it.longitude)
                    }
                }
            } catch (e: Exception) {
                val lastLocation = locationManager.getLastKnownLocation()
                _ubicacion.value = lastLocation
                lastLocation?.let {
                    permissionPreferences.setLastLocation(it.latitude, it.longitude)
                }
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun tienePermiso(): Boolean = locationManager.hasLocationPermission()


    // La lógica de estadísticas se movió a UsuarioViewModel para mayor atomicidad
    fun reportarRapido(tipoReporte: String, onExito: () -> Unit = {}) {
        val uid = auth.currentUser?.uid ?: "Invitado_Default"
        val currentUbicacion = _ubicacion.value

        if (currentUbicacion == null) {
            errorReporte = "📍 Se requiere ubicación"
            return
        }

        cargandoReporte = true
        errorReporte = null
        reporteExito = false

        val ubicacionCoords = listOf(
            currentUbicacion.latitude,
            currentUbicacion.longitude
        )

        val docRef = firestore.collection("reportes").document()

        val reporte = Reporte(
            id = docRef.id,
            id_usuario = uid,
            descripcion = "Reporte rápido: $tipoReporte",
            fechapublicacion = System.currentTimeMillis(),
            tipo = tipoReporte,
            ubicacion = ubicacionCoords
        )

        docRef.set(reporte)
            .addOnSuccessListener {
                cargandoReporte = false
                reporteExito = true
                onExito() // Callback para que UsuarioViewModel actualice estadísticas
            }
            .addOnFailureListener { error ->
                cargandoReporte = false
                errorReporte = "❌ Error: ${error.message}"
            }
    }


}
