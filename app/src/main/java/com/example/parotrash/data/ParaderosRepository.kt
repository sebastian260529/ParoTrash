package com.example.parotrash.data

import android.util.Log
import com.example.parotrash.modelos.ParaderoSITP
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL

class ParaderosRepository {

    private val API_URL = "https://gis.transmilenio.gov.co/arcgis/rest/services/Zonal/consulta_paraderos/FeatureServer/0/query?where=1%3D1&outFields=*&outSR=4326&f=json"
    private val TIMEOUT_MS = 15000

    suspend fun obtenerParaderos(): Result<List<ParaderoSITP>> = withContext(Dispatchers.IO) {
        var conexion: HttpURLConnection? = null
        try {
            val url = URL(API_URL)
            conexion = url.openConnection() as HttpURLConnection
            conexion.requestMethod = "GET"
            conexion.connectTimeout = TIMEOUT_MS
            conexion.readTimeout = TIMEOUT_MS
            conexion.setRequestProperty("Accept", "application/json")

            val codigoRespuesta = conexion.responseCode
            if (codigoRespuesta < 200 || codigoRespuesta >= 300) {
                Log.e("ParaderosRepo", "Error HTTP: código $codigoRespuesta")
                return@withContext Result.failure(Exception("Error del servidor: código $codigoRespuesta"))
            }

            val respuesta = conexion.inputStream.bufferedReader().readText()
            if (respuesta.isBlank()) {
                Log.e("ParaderosRepo", "Respuesta vacía")
                return@withContext Result.failure(Exception("Respuesta vacía del servidor"))
            }

            val json = JSONObject(respuesta)
            val features = json.optJSONArray("features")
            if (features == null || features.length() == 0) {
                Log.e("ParaderosRepo", "No se encontró arreglo features")
                return@withContext Result.failure(Exception("No se encontraron paraderos"))
            }

            val paraderos = mutableListOf<ParaderoSITP>()
            for (i in 0 until features.length()) {
                try {
                    val feature = features.getJSONObject(i)
                    val attributes = feature.optJSONObject("attributes")

                    if (attributes == null) continue

                    val nombre = attributes.optString("nombre").ifBlank { "Paradero sin nombre" }
                    val direccion = attributes.optString("direccion_bandera").takeIf { it.isNotBlank() }
                    val zona = attributes.optString("zona_sitp", "N/A")

                    val lng = attributes.optDouble("longitud", Double.NaN)
                    val lat = attributes.optDouble("latitud", Double.NaN)

                    if (lng.isNaN() || lat.isNaN()) {
                        continue
                    }

                    paraderos.add(
                        ParaderoSITP(
                            nombre = nombre,
                            longitud = lng,
                            latitud = lat,
                            direccion = direccion,
                            zona = zona
                        )
                    )
                } catch (e: Exception) {
                    Log.w("ParaderosRepo", "Error parseando paradero $i: ${e.message}")
                }
            }

            Log.d("ParaderosRepo", "Paraderos parseados: ${paraderos.size}")
            Result.success(paraderos)

        } catch (e: Exception) {
            Log.e("ParaderosRepo", "Error de red: ${e.message}")
            Result.failure(e)
        } finally {
            conexion?.disconnect()
        }
    }
}