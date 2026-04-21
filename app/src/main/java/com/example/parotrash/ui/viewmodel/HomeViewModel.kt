package com.example.parotrash.ui.viewmodel

import android.app.Application
import android.location.Location
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.parotrash.data.LocationManager
import com.example.parotrash.data.SessionManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class HomeViewModel(application: Application) : AndroidViewModel(application) {

    private val locationManager = LocationManager(application)

    private val _ubicacion = MutableStateFlow<Location?>(null)
    val ubicacion: StateFlow<Location?> = _ubicacion

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

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