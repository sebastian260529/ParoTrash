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

    fun obtenerUbicacion() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val location = locationManager.getCurrentLocation()
                if (location != null) {
                    _ubicacion.value = location
                } else {
                    // Si no puede obtener actual, usar última conocida
                    val lastLocation = locationManager.getLastKnownLocation()
                    _ubicacion.value = lastLocation
                }
            } catch (e: Exception) {
                // Si falla, intentar última conocida
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