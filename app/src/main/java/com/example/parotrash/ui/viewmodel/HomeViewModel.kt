package com.example.parotrash.ui.viewmodel

import android.app.Application
import android.location.Location
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.parotrash.data.LocationManager
import com.example.parotrash.data.SessionManager
import com.example.parotrash.modelos.Reporte
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class HomeViewModel(application: Application) : AndroidViewModel(application) {

    private val locationManager = LocationManager(application)
    private val auth = FirebaseAuth.getInstance()
    private val firestore = FirebaseFirestore.getInstance()

    private val _ubicacion = MutableStateFlow<Location?>(null)
    val ubicacion: StateFlow<Location?> = _ubicacion

    private val _reportes = MutableStateFlow<List<Reporte>>(emptyList())
    val reportes: StateFlow<List<Reporte>> = _reportes

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

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
    }

    private fun escucharReportes() {
        firestore.collection("reportes")
            .addSnapshotListener { snapshot, e ->
                if (e != null) {
                    errorReporte = "❌ Error al cargar reportes: ${e.message}"
                    return@addSnapshotListener
                }

                if (snapshot != null) {
                    try {
                        val listaReportes = snapshot.documents.mapNotNull { doc ->
                            doc.toObject(Reporte::class.java)?.copy(id = doc.id)
                        }
                        _reportes.value = listaReportes
                    } catch (ex: Exception) {
                        errorReporte = "❌ Error al procesar datos: ${ex.message}"
                    }
                }
            }
    }

    fun eliminarReporte(reporteId: String) {
        if (reporteId.isEmpty()) return

        firestore.collection("reportes")
            .document(reporteId)
            .delete()
            .addOnFailureListener { error ->
                errorReporte = "❌ No se pudo eliminar el reporte: ${error.message}"
            }
    }

    fun limpiarError() {
        errorReporte = null
    }

    fun obtenerUbicacion() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val location = locationManager.getCurrentLocation()
                if (location != null) {
                    _ubicacion.value = location
                } else {
                    val lastLocation = locationManager.getLastKnownLocation()
                    _ubicacion.value = lastLocation
                }
            } catch (e: Exception) {
                val lastLocation = locationManager.getLastKnownLocation()
                _ubicacion.value = lastLocation
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun tienePermiso(): Boolean = locationManager.hasLocationPermission()

    fun actualizarDescripcion(nuevaDescripcion: String) {
        descripcion = nuevaDescripcion
        errorReporte = null
    }

    fun actualizarTipo(nuevoTipo: String) {
        tipo = nuevoTipo
        errorReporte = null
    }

    // Función para reportes rápidos desde los diálogos
    fun reportarRapido(tipoReporte: String) {
        val uid = auth.currentUser?.uid
        val currentUbicacion = _ubicacion.value

        if (uid == null || currentUbicacion == null) {
            errorReporte = if (uid == null) "⚠️ Debes iniciar sesión" else "📍 Se requiere ubicación"
            return
        }

        cargandoReporte = true
        errorReporte = null

        val ubicacionCoords = listOf(
            currentUbicacion.latitude,
            currentUbicacion.longitude
        )

        val reporte = Reporte(
            id_usuario = uid,
            descripcion = "Reporte rápido: $tipoReporte",
            fechapublicacion = System.currentTimeMillis(),
            tipo = tipoReporte,
            ubicacion = ubicacionCoords
        )

        firestore.collection("reportes")
            .add(reporte)
            .addOnSuccessListener {
                cargandoReporte = false
                reporteExito = true
            }
            .addOnFailureListener { error ->
                cargandoReporte = false
                errorReporte = "❌ Error: ${error.message}"
            }
    }

    fun subirReporte() {
        var hayError = false

        if (descripcion.isEmpty()) {
            errorReporte = "📝 La descripción no puede estar vacía"
            hayError = true
        }
        if (tipo.isEmpty()) {
            errorReporte = "🏷️ Selecciona un tipo de reporte"
            hayError = true
        }
        if (_ubicacion.value == null) {
            errorReporte = "📍 Se requiere ubicación"
            hayError = true
        }

        if (hayError) return

        val uid = auth.currentUser?.uid
        if (uid == null) {
            errorReporte = "⚠️ Debes iniciar sesión"
            return
        }

        cargandoReporte = true
        errorReporte = null

        val ubicacionCoords = listOf(
            _ubicacion.value!!.latitude,
            _ubicacion.value!!.longitude
        )

        val reporte = Reporte(
            id_usuario = uid,
            descripcion = descripcion,
            fechapublicacion = System.currentTimeMillis(),
            tipo = tipo,
            ubicacion = ubicacionCoords
        )

        firestore.collection("reportes")
            .add(reporte)
            .addOnSuccessListener {
                cargandoReporte = false
                reporteExito = true
                descripcion = ""
                tipo = ""
            }
            .addOnFailureListener { error ->
                cargandoReporte = false
                errorReporte = "❌ Error al subir reporte: ${error.message}"
            }
    }

    fun cerrarSesion(sessionManager: SessionManager, onComplete: () -> Unit) {
        viewModelScope.launch {
            _isLoading.value = true
            sessionManager.logout()
            _ubicacion.value = null
            _isLoading.value = false
            onComplete()
        }
    }
}
