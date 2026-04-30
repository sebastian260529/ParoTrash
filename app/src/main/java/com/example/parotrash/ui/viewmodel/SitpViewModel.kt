package com.example.parotrash.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.parotrash.data.ParaderosRepository
import com.example.parotrash.modelos.ParaderoSITP
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class SitpViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = ParaderosRepository()

    private val _paraderos = MutableStateFlow<List<ParaderoSITP>>(emptyList())
    val paraderos: StateFlow<List<ParaderoSITP>> = _paraderos

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    private val _resultadosBusqueda = MutableStateFlow<List<ParaderoSITP>>(emptyList())
    val resultadosBusqueda: StateFlow<List<ParaderoSITP>> = _resultadosBusqueda

    init {
        cargarParaderos()
    }

    fun cargarParaderos() {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null

            val resultado = repository.obtenerParaderos()
            resultado.onSuccess { lista ->
                _paraderos.value = lista
            }.onFailure { e ->
                _error.value = e.message ?: "Error al cargar paraderos"
            }

            _isLoading.value = false
        }
    }

    fun buscarParaderos(texto: String) {
        if (texto.isBlank()) {
            _resultadosBusqueda.value = emptyList()
            return
        }
        val filtrados = _paraderos.value.filter {
            it.nombre.contains(texto, ignoreCase = true) ||
            it.direccion?.contains(texto, ignoreCase = true) == true
        }
        _resultadosBusqueda.value = filtrados.take(10)
    }
}