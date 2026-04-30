package com.example.parotrash.ui.viewmodel

import android.app.Application
import android.location.Location
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.parotrash.data.BusquedaLugaresRepository
import com.example.parotrash.data.LocationManager
import com.example.parotrash.data.RutasFavoritasRepository
import com.example.parotrash.modelos.LugarBusqueda
import com.example.parotrash.modelos.RutaFavorita
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class RutasFavoritasViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = RutasFavoritasRepository()
    private val busquedaRepository = BusquedaLugaresRepository()
    private val locationManager = LocationManager(application)
    private val auth = FirebaseAuth.getInstance()

    private val _rutas = MutableStateFlow<List<RutaFavorita>>(emptyList())
    val rutas: StateFlow<List<RutaFavorita>> = _rutas

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    private val _resultadoBusqueda = MutableStateFlow<List<LugarBusqueda>>(emptyList())
    val resultadoBusqueda: StateFlow<List<LugarBusqueda>> = _resultadoBusqueda

    private val _seleccionTemporal = MutableStateFlow<LugarBusqueda?>(null)
    val seleccionTemporal: StateFlow<LugarBusqueda?> = _seleccionTemporal

    private val _ubicacionActual = MutableStateFlow<Location?>(null)
    val ubicacionActual: StateFlow<Location?> = _ubicacionActual

    enum class EstadoSelector { ESPERANDO, TRANSMILENIO, SITP, ALIMENTADOR }
    private val _estadoSelector = MutableStateFlow(EstadoSelector.ESPERANDO)
    val estadoSelector: StateFlow<EstadoSelector> = _estadoSelector

    init {
        cargarRutas()
    }

    fun cargarRutas() {
        val uid = auth.currentUser?.uid ?: return
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null

            val resultado = repository.obtenerRutasPorUsuario(uid)
            resultado.onSuccess { lista ->
                _rutas.value = lista
            }.onFailure { e ->
                _error.value = e.message
                Log.e("RutasVM", "Error: ${e.message}")
            }

            _isLoading.value = false
        }
    }

    fun buscarLugares(texto: String) {
        viewModelScope.launch {
            val resultados = busquedaRepository.buscarLugares(texto)
            _resultadoBusqueda.value = resultados
        }
    }

    fun guardarRuta(
        nombre: String,
        desdeNombre: String,
        desdeLat: Double,
        desdeLng: Double,
        hastaNombre: String,
        hastaLat: Double,
        hastaLng: Double,
        onSuccess: () -> Unit
    ) {
        val uid = auth.currentUser?.uid ?: run {
            _error.value = "Usuario no autenticado"
            return
        }

        viewModelScope.launch {
            _isLoading.value = true

            val ruta = RutaFavorita(
                id_usuario = uid,
                nombre = nombre,
                desdeNombre = desdeNombre,
                desdeLat = desdeLat,
                desdeLng = desdeLng,
                hastaNombre = hastaNombre,
                hastaLat = hastaLat,
                hastaLng = hastaLng
            )

            val resultado = repository.guardarRuta(ruta)
            resultado.onSuccess {
                cargarRutas()
                onSuccess()
            }.onFailure { e ->
                _error.value = e.message
            }

            _isLoading.value = false
        }
    }

    fun actualizarRuta(ruta: RutaFavorita, onSuccess: () -> Unit) {
        viewModelScope.launch {
            _isLoading.value = true

            val resultado = repository.actualizarRuta(ruta)
            resultado.onSuccess {
                cargarRutas()
                onSuccess()
            }.onFailure { e ->
                _error.value = e.message
            }

            _isLoading.value = false
        }
    }

    fun eliminarRuta(id: String, onSuccess: () -> Unit) {
        viewModelScope.launch {
            _isLoading.value = true

            val resultado = repository.eliminarRuta(id)
            resultado.onSuccess {
                cargarRutas()
                onSuccess()
            }.onFailure { e ->
                _error.value = e.message
            }

            _isLoading.value = false
        }
    }

    fun iniciarSelectorTransmilenio() {
        _estadoSelector.value = EstadoSelector.TRANSMILENIO
    }

    fun iniciarSelectorSITP() {
        _estadoSelector.value = EstadoSelector.SITP
    }

    fun iniciarSelectorAlimentador() {
        _estadoSelector.value = EstadoSelector.ALIMENTADOR
    }

    fun obtenerMiUbicacion(onComplete: (LugarBusqueda?) -> Unit) {
        viewModelScope.launch {
            try {
                val location = locationManager.getCurrentLocation()
                    ?: locationManager.getLastKnownLocation()
                _ubicacionActual.value = location

                if (location != null) {
                    val lugar = LugarBusqueda(
                        nombre = "Mi ubicación",
                        latitud = location.latitude,
                        longitud = location.longitude,
                        tipo = "ubicacion_actual",
                        direccion = null
                    )
                    onComplete(lugar)
                } else {
                    _error.value = "No se pudo obtener ubicación"
                    onComplete(null)
                }
            } catch (e: Exception) {
                _error.value = "Error al obtener ubicación: ${e.message}"
                onComplete(null)
            }
        }
    }

    fun buscarEstaciones(texto: String) {
        viewModelScope.launch {
            try {
                val resultados = busquedaRepository.buscarLugares(texto)
                val soloEstaciones = resultados.filter { it.tipo == "estacion" }
                _resultadoBusqueda.value = soloEstaciones.take(20)
            } catch (e: Exception) {
                _error.value = "Error en búsqueda: ${e.message}"
            }
        }
    }

    fun guardarSeleccionTemporal(lugar: LugarBusqueda) {
        _seleccionTemporal.value = lugar
    }

    fun obtenerSeleccionTemporal(): LugarBusqueda? {
        val seleccion = _seleccionTemporal.value
        _seleccionTemporal.value = null
        _estadoSelector.value = EstadoSelector.ESPERANDO
        return seleccion
    }

    fun limpiarSeleccion() {
        _seleccionTemporal.value = null
        _estadoSelector.value = EstadoSelector.ESPERANDO
    }

    fun clearError() {
        _error.value = null
    }
}