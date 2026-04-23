package com.example.parotrash.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.parotrash.data.NotificationPreferences
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class NotificacionesViewModel(
    private val preferences: NotificationPreferences
) : ViewModel() {

    // Estados (el setter ya está incluido automáticamente)
    var recibirAlertas by mutableStateOf(true)
        private set

    var bloqueosViales by mutableStateOf(true)
        private set

    var manifestaciones by mutableStateOf(true)
        private set

    var comunidad by mutableStateOf(true)
        private set

    init {
        cargarPreferencias()
    }

    private fun cargarPreferencias() {
        viewModelScope.launch {
            preferences.recibirAlertas.collectLatest { recibirAlertas = it }
        }
        viewModelScope.launch {
            preferences.bloqueosViales.collectLatest { bloqueosViales = it }
        }
        viewModelScope.launch {
            preferences.manifestaciones.collectLatest { manifestaciones = it }
        }
        viewModelScope.launch {
            preferences.comunidad.collectLatest { comunidad = it }
        }
    }

    // ✅ Estas son las ÚNICAS funciones que necesitas
    fun actualizarRecibirAlertas(valor: Boolean) {
        recibirAlertas = valor
        viewModelScope.launch {
            preferences.setRecibirAlertas(valor)
        }
    }

    fun actualizarBloqueosViales(valor: Boolean) {
        bloqueosViales = valor
        viewModelScope.launch {
            preferences.setBloqueosViales(valor)
        }
    }

    fun actualizarManifestaciones(valor: Boolean) {
        manifestaciones = valor
        viewModelScope.launch {
            preferences.setManifestaciones(valor)
        }
    }

    fun actualizarComunidad(valor: Boolean) {
        comunidad = valor
        viewModelScope.launch {
            preferences.setComunidad(valor)
        }
    }
}