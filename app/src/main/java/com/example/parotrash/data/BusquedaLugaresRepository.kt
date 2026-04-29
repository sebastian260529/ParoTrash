package com.example.parotrash.data

import android.util.Log
import com.example.parotrash.modelos.EstacionTransmilenio
import com.example.parotrash.modelos.LugarBusqueda
import com.example.parotrash.modelos.ParaderoSITP
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL

class BusquedaLugaresRepository {

    private val API_ESTACIONES = "https://gis.transmilenio.gov.co/arcgis/rest/services/Tecnica/Consulta_Informacion_Geografica_STS/FeatureServer/2/query?where=1%3D1&outFields=num_est,nom_est,id_trazado,num_vag,esta_oper,eta_oper,tipo_esta,ub_est,num_acc&outSR=4326&f=json"
    private val API_PARADEROS = "https://gis.transmilenio.gov.co/arcgis/rest/services/Zonal/consulta_paraderos/FeatureServer/0/query?where=1%3D1&outFields=*&outSR=4326&f=json"
    private val TIMEOUT_MS = 15000

    private var cacheEstaciones: List<LugarBusqueda>? = null
    private var cacheParaderos: List<LugarBusqueda>? = null

    suspend fun buscarLugares(texto: String): List<LugarBusqueda> = withContext(Dispatchers.IO) {
        if (cacheEstaciones == null || cacheParaderos == null) {
            cargarDatos()
        }

        val todosLugares = (cacheEstaciones ?: emptyList()) + (cacheParaderos ?: emptyList())
        
        if (texto.isBlank()) {
            return@withContext todosLugares.take(50)
        }

        val filtrados = todosLugares.filter { lugar ->
            lugar.nombre.contains(texto, ignoreCase = true) ||
            lugar.direccion?.contains(texto, ignoreCase = true) == true
        }

        filtrados.take(20)
    }

    private suspend fun cargarDatos() {
        var conexion: HttpURLConnection? = null
        try {
            val url = URL(API_ESTACIONES)
            conexion = url.openConnection() as HttpURLConnection
            conexion.requestMethod = "GET"
            conexion.connectTimeout = TIMEOUT_MS
            conexion.readTimeout = TIMEOUT_MS
            conexion.setRequestProperty("Accept", "application/json")

            val respuesta = conexion.inputStream.bufferedReader().readText()
            val json = JSONObject(respuesta)
            val features = json.optJSONArray("features") ?: return

            val estaciones = mutableListOf<LugarBusqueda>()
            for (i in 0 until features.length()) {
                try {
                    val feature = features.getJSONObject(i)
                    val attributes = feature.optJSONObject("attributes")
                    val geometry = feature.optJSONObject("geometry")

                    if (attributes == null || geometry == null) continue

                    val nombre = attributes.optString("nom_est")
                    if (nombre.isBlank()) continue

                    val lng = geometry.optDouble("x", Double.NaN)
                    val lat = geometry.optDouble("y", Double.NaN)
                    if (lng.isNaN() || lat.isNaN()) continue

                    val direccion = attributes.optString("ub_est").takeIf { it.isNotBlank() }

                    estaciones.add(
                        LugarBusqueda(
                            nombre = nombre,
                            latitud = lat,
                            longitud = lng,
                            tipo = "estacion",
                            direccion = direccion
                        )
                    )
                } catch (e: Exception) { }
            }
            cacheEstaciones = estaciones

        } catch (e: Exception) {
            Log.e("BusquedaLugares", "Error cargando estaciones: ${e.message}")
            cacheEstaciones = emptyList()
        } finally {
            conexion?.disconnect()
        }

        try {
            val url2 = URL(API_PARADEROS)
            conexion = url2.openConnection() as HttpURLConnection
            conexion.requestMethod = "GET"
            conexion.connectTimeout = TIMEOUT_MS
            conexion.readTimeout = TIMEOUT_MS

            val respuesta = conexion.inputStream.bufferedReader().readText()
            val json = JSONObject(respuesta)
            val features = json.optJSONArray("features") ?: return

            val paraderos = mutableListOf<LugarBusqueda>()
            for (i in 0 until features.length()) {
                try {
                    val feature = features.getJSONObject(i)
                    val attributes = feature.optJSONObject("attributes")

                    if (attributes == null) continue

                    val nombre = attributes.optString("nombre")
                    if (nombre.isBlank()) continue

                    val lng = attributes.optDouble("longitud", Double.NaN)
                    val lat = attributes.optDouble("latitud", Double.NaN)
                    if (lng.isNaN() || lat.isNaN()) continue

                    val direccion = attributes.optString("direccion_bandera").takeIf { it.isNotBlank() }

                    paraderos.add(
                        LugarBusqueda(
                            nombre = nombre,
                            latitud = lat,
                            longitud = lng,
                            tipo = "paradero",
                            direccion = direccion
                        )
                    )
                } catch (e: Exception) { }
            }
            cacheParaderos = paraderos

        } catch (e: Exception) {
            Log.e("BusquedaLugares", "Error cargando paraderos: ${e.message}")
            cacheParaderos = emptyList()
        } finally {
            conexion?.disconnect()
        }
    }
}