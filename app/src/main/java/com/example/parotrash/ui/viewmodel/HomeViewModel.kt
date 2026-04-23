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
    private val auth = FirebaseAuth.getInstance()
    private val firestore = FirebaseFirestore.getInstance()

    private val _ubicacion = MutableStateFlow<Location?>(null)
    val ubicacion: StateFlow<Location?> = _ubicacion

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _permisoConcedido = MutableStateFlow(locationManager.hasLocationPermission())
    val permisoConcedido: StateFlow<Boolean> = _permisoConcedido

    private val _reportes = MutableStateFlow<List<Reporte>>(emptyList())
    val reportes: StateFlow<List<Reporte>> = _reportes

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
                            }
                        } catch (e: Exception) {
                            Log.e("HomeViewModel", "Error al procesar reporte ${doc.id}: ${e.message}")
                        }
                    }
                    _reportes.value = listaValida
                }
            }
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
