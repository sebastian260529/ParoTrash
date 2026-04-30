package com.example.parotrash.ui.viewmodel

import android.app.Application
import android.content.Context
import android.location.Location
import android.provider.Settings
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.application
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
    private val prefs = application.getSharedPreferences("rutas_temp", Context.MODE_PRIVATE)

    companion object {
        fun guardarSeleccionTemp(context: Context, nombre: String, lat: Double, lng: Double, tipo: String, campo: String = "") {
            context.getSharedPreferences("rutas_temp", Context.MODE_PRIVATE)
                .edit()
                .putString("sel_nombre", nombre)
                .putString("sel_lat", lat.toString())
                .putString("sel_lng", lng.toString())
                .putString("sel_tipo", tipo)
                .putString("sel_campo", campo)
                .apply()
        }

        fun guardarDesdeTemp(context: Context, nombre: String, lat: Double, lng: Double, tipo: String) {
            context.getSharedPreferences("rutas_temp", Context.MODE_PRIVATE)
                .edit()
                .putString("desde_nombre", nombre)
                .putString("desde_lat", lat.toString())
                .putString("desde_lng", lng.toString())
                .putString("desde_tipo", tipo)
                .apply()
        }

        fun guardarHastaTemp(context: Context, nombre: String, lat: Double, lng: Double, tipo: String) {
            context.getSharedPreferences("rutas_temp", Context.MODE_PRIVATE)
                .edit()
                .putString("hasta_nombre", nombre)
                .putString("hasta_lat", lat.toString())
                .putString("hasta_lng", lng.toString())
                .putString("hasta_tipo", tipo)
                .apply()
        }

        fun obtenerDesdeTemp(context: Context): LugarBusqueda? {
            val prefs = context.getSharedPreferences("rutas_temp", Context.MODE_PRIVATE)
            val nombre = prefs.getString("desde_nombre", null) ?: return null
            val lat = prefs.getString("desde_lat", "0.0")?.toDoubleOrNull() ?: 0.0
            val lng = prefs.getString("desde_lng", "0.0")?.toDoubleOrNull() ?: 0.0
            val tipo = prefs.getString("desde_tipo", "") ?: ""
            return LugarBusqueda(nombre, lat, lng, tipo)
        }

        fun obtenerHastaTemp(context: Context): LugarBusqueda? {
            val prefs = context.getSharedPreferences("rutas_temp", Context.MODE_PRIVATE)
            val nombre = prefs.getString("hasta_nombre", null) ?: return null
            val lat = prefs.getString("hasta_lat", "0.0")?.toDoubleOrNull() ?: 0.0
            val lng = prefs.getString("hasta_lng", "0.0")?.toDoubleOrNull() ?: 0.0
            val tipo = prefs.getString("hasta_tipo", "") ?: ""
            return LugarBusqueda(nombre, lat, lng, tipo)
        }

        fun obtenerSeleccionTemp(context: Context): LugarBusqueda? {
            val prefs = context.getSharedPreferences("rutas_temp", Context.MODE_PRIVATE)
            val nombre = prefs.getString("sel_nombre", null) ?: return null
            val lat = prefs.getString("sel_lat", "0.0")?.toDoubleOrNull() ?: 0.0
            val lng = prefs.getString("sel_lng", "0.0")?.toDoubleOrNull() ?: 0.0
            val tipo = prefs.getString("sel_tipo", "") ?: ""
            return LugarBusqueda(nombre, lat, lng, tipo)
        }

        fun obtenerCampoActivoTemp(context: Context): String {
            val prefs = context.getSharedPreferences("rutas_temp", Context.MODE_PRIVATE)
            return prefs.getString("sel_campo", "") ?: ""
        }

        fun limpiarSeleccionTemp(context: Context) {
            context.getSharedPreferences("rutas_temp", Context.MODE_PRIVATE)
                .edit()
                .clear()
                .apply()
        }
    }

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

    private val _campoActivo = MutableStateFlow("")
    val campoActivo: StateFlow<String> = _campoActivo

    fun guardarCampoActivo(campo: String) {
        _campoActivo.value = campo
        prefs.edit().putString("campo_activo", campo).apply()
    }

    fun obtenerCampoActivo(): String {
        val campo = _campoActivo.value.ifEmpty { 
            prefs.getString("campo_activo", "") ?: ""
        }
        return campo
    }

    fun limpiarCampoActivo() {
        _campoActivo.value = ""
        prefs.edit().remove("campo_activo").apply()
    }

    init {
        cargarRutas()
    }

    private fun getDeviceId(): String {
        val prefs = application.getSharedPreferences("rutas_favoritas_prefs", Context.MODE_PRIVATE)
        var deviceId = prefs.getString("device_id", null)
        if (deviceId == null) {
            deviceId = "device_${System.currentTimeMillis()}_${(0..99999).random()}"
            prefs.edit().putString("device_id", deviceId).apply()
        }
        return deviceId
    }

    fun cargarRutas() {
        val uid = auth.currentUser?.uid ?: getDeviceId()
        
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null

            try {
                val resultado = repository.obtenerRutasPorUsuario(uid)
                resultado.onSuccess { lista ->
                    _rutas.value = lista
                    if (lista.isEmpty() && auth.currentUser == null) {
                        repository.crearUsuarioSiNoExiste(uid)
                    }
                }.onFailure { e ->
                    _error.value = "Error al cargar rutas: ${e.message}"
                    Log.e("RutasVM", "Error cargar rutas: ${e.message}")
                }
            } catch (e: Exception) {
                _error.value = "Error de conexión: ${e.message}"
                Log.e("RutasVM", "Exception: ${e.message}")
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun buscarLugares(texto: String) {
        if (texto.isBlank()) {
            _resultadoBusqueda.value = emptyList()
            return
        }
        
        viewModelScope.launch {
            try {
                val resultados = busquedaRepository.buscarLugares(texto)
                _resultadoBusqueda.value = resultados
            } catch (e: Exception) {
                _error.value = "Error en búsqueda: ${e.message}"
                Log.e("RutasVM", "Error búsqueda: ${e.message}")
            }
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
        onSuccess: () -> Unit,
        onError: (String) -> Unit = {}
    ) {
        val uid = auth.currentUser?.uid ?: getDeviceId()

        if (nombre.isBlank()) {
            _error.value = "El nombre es obligatorio"
            onError("El nombre es obligatorio")
            return
        }

        if (desdeLat == 0.0 || hastaLat == 0.0) {
            _error.value = "SeleccionaDesde y Destino"
            onError("Selecciona Desde y Destino")
            return
        }

        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null

            try {
                val ruta = RutaFavorita(
                    id_usuario = uid,
                    nombre = nombre.trim(),
                    desdeNombre = desdeNombre.trim(),
                    desdeLat = desdeLat,
                    desdeLng = desdeLng,
                    hastaNombre = hastaNombre.trim(),
                    hastaLat = hastaLat,
                    hastaLng = hastaLng
                )

                val resultado = repository.guardarRuta(ruta)
                resultado.onSuccess {
                    cargarRutas()
                    onSuccess()
                }.onFailure { e ->
                    val msg = "Error al guardar: ${e.message}"
                    _error.value = msg
                    onError(msg)
                    Log.e("RutasVM", msg)
                }
            } catch (e: Exception) {
                val msg = "Error de conexión: ${e.message}"
                _error.value = msg
                onError(msg)
                Log.e("RutasVM", msg)
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun actualizarRuta(ruta: RutaFavorita, onSuccess: () -> Unit) {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null

            try {
                val resultado = repository.actualizarRuta(ruta)
                resultado.onSuccess {
                    cargarRutas()
                    onSuccess()
                }.onFailure { e ->
                    _error.value = "Error al actualizar: ${e.message}"
                }
            } catch (e: Exception) {
                _error.value = "Error: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun eliminarRuta(id: String, onSuccess: () -> Unit) {
        if (id.isBlank()) {
            _error.value = "ID de ruta inválido"
            return
        }
        
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null

            try {
                val resultado = repository.eliminarRuta(id)
                resultado.onSuccess {
                    cargarRutas()
                    onSuccess()
                }.onFailure { e ->
                    _error.value = "Error al eliminar: ${e.message}"
                }
            } catch (e: Exception) {
                _error.value = "Error: ${e.message}"
            } finally {
                _isLoading.value = false
            }
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
                if (!locationManager.hasLocationPermission()) {
                    _error.value = "Permiso de ubicación denegado"
                    onComplete(null)
                    return@launch
                }
                
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
                    _error.value = "No se pudo obtener ubicación. Verifica que el GPS esté activado."
                    onComplete(null)
                }
            } catch (e: SecurityException) {
                _error.value = "Permiso de ubicación requerido"
                Log.e("RutasVM", "Security: ${e.message}")
                onComplete(null)
            } catch (e: Exception) {
                _error.value = "Error al obtener ubicación: ${e.message}"
                Log.e("RutasVM", "Ubicación error: ${e.message}")
                onComplete(null)
            }
        }
    }

    fun buscarEstaciones(texto: String) {
        if (texto.isBlank()) {
            _resultadoBusqueda.value = emptyList()
            return
        }
        
        viewModelScope.launch {
            _error.value = null
            try {
                val resultados = busquedaRepository.buscarLugares(texto)
                val soloEstaciones = resultados.filter { it.tipo == "estacion" }
                _resultadoBusqueda.value = soloEstaciones.take(20)
                
                if (soloEstaciones.isEmpty()) {
                    Log.d("RutasVM", "No se encontraron estaciones para: $texto")
                }
            } catch (e: Exception) {
                _error.value = "Error en búsqueda: ${e.message}"
                Log.e("RutasVM", "Error búsqueda: ${e.message}")
            }
        }
    }

    fun guardarSeleccionTemporal(lugar: LugarBusqueda) {
        _seleccionTemporal.value = lugar
        val campo = obtenerCampoActivo()
        guardarSeleccionTemp(getApplication(), lugar.nombre, lugar.latitud, lugar.longitud, lugar.tipo, campo)
        
        when (campo) {
            "desde" -> guardarDesdeTemp(getApplication(), lugar.nombre, lugar.latitud, lugar.longitud, lugar.tipo)
            "destino" -> guardarHastaTemp(getApplication(), lugar.nombre, lugar.latitud, lugar.longitud, lugar.tipo)
        }
    }

    fun obtenerSeleccionTemporal(): LugarBusqueda? {
        val seleccion = _seleccionTemporal.value ?: obtenerSeleccionTemp(getApplication())
        _seleccionTemporal.value = null
        _estadoSelector.value = EstadoSelector.ESPERANDO
        return seleccion
    }

    fun limpiarSeleccion() {
        _seleccionTemporal.value = null
        _estadoSelector.value = EstadoSelector.ESPERANDO
        _campoActivo.value = ""
    }

    fun obtenerDesdeTemporal(): LugarBusqueda? {
        return obtenerDesdeTemp(getApplication())
    }

    fun obtenerHastaTemporal(): LugarBusqueda? {
        return obtenerHastaTemp(getApplication())
    }

    fun guardarDesdeTemporal(lugar: LugarBusqueda) {
        guardarDesdeTemp(getApplication(), lugar.nombre, lugar.latitud, lugar.longitud, lugar.tipo)
    }

    fun guardarHastaTemporal(lugar: LugarBusqueda) {
        guardarHastaTemp(getApplication(), lugar.nombre, lugar.latitud, lugar.longitud, lugar.tipo)
    }

    fun clearError() {
        _error.value = null
    }
}