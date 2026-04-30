package com.example.parotrash.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.parotrash.data.EstacionesRepository
import com.example.parotrash.modelos.EstacionTransmilenio
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class EstacionesViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = EstacionesRepository()

    private val _estaciones = MutableStateFlow<List<EstacionTransmilenio>>(emptyList())
    val estaciones: StateFlow<List<EstacionTransmilenio>> = _estaciones

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    private val _resultadosBusqueda = MutableStateFlow<List<EstacionTransmilenio>>(emptyList())
    val resultadosBusqueda: StateFlow<List<EstacionTransmilenio>> = _resultadosBusqueda

    init {
        cargarEstaciones()
    }

    fun cargarEstaciones() {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null

            val resultado = repository.obtenerEstaciones()
            resultado.onSuccess { lista ->
                _estaciones.value = lista
            }.onFailure { e ->
                _error.value = e.message ?: "Error al cargar estaciones"
            }

            _isLoading.value = false
        }
    }

    fun buscarEstaciones(texto: String) {
        if (texto.isBlank()) {
            _resultadosBusqueda.value = emptyList()
            return
        }
        val filtradas = _estaciones.value.filter {
            it.nomEst.contains(texto, ignoreCase = true) ||
            it.ubEst?.contains(texto, ignoreCase = true) == true
        }
        _resultadosBusqueda.value = filtradas.take(10)
    }
}