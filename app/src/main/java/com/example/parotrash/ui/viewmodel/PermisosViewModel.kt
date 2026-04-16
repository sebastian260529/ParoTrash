package com.example.parotrash.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.parotrash.data.PermissionPreferences
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class PermisosViewModel(
    private val preferences: PermissionPreferences
) : ViewModel() {

    var ubicacion by mutableStateOf(false)
        private set

    var notificaciones by mutableStateOf(false)
        private set

    var almacenamiento by mutableStateOf(false)
        private set

    init {
        cargarPreferencias()
    }

    private fun cargarPreferencias() {
        viewModelScope.launch {
            preferences.ubicacion.collectLatest { ubicacion = it }
        }
        viewModelScope.launch {
            preferences.notificaciones.collectLatest { notificaciones = it }
        }
        viewModelScope.launch {
            preferences.almacenamiento.collectLatest { almacenamiento = it }
        }
    }

    fun actualizarUbicacion(concedido: Boolean) {
        ubicacion = concedido
        viewModelScope.launch {
            preferences.setUbicacion(concedido)
        }
    }

    fun actualizarNotificaciones(concedido: Boolean) {
        notificaciones = concedido
        viewModelScope.launch {
            preferences.setNotificaciones(concedido)
        }
    }

    fun actualizarAlmacenamiento(concedido: Boolean) {
        almacenamiento = concedido
        viewModelScope.launch {
            preferences.setAlmacenamiento(concedido)
        }
    }
}