package com.example.parotrash.data

import android.util.Log
import com.example.parotrash.modelos.EstacionTransmilenio
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL

class EstacionesRepository {

    private val API_URL = "https://gis.transmilenio.gov.co/arcgis/rest/services/Tecnica/Consulta_Informacion_Geografica_STS/FeatureServer/2/query?where=1%3D1&outFields=num_est,nom_est,id_trazado,num_vag,esta_oper,eta_oper,tipo_esta,ub_est,num_acc&outSR=4326&f=json"
    private val TIMEOUT_MS = 10000

    suspend fun obtenerEstaciones(): Result<List<EstacionTransmilenio>> = withContext(Dispatchers.IO) {
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
                Log.e("EstacionesRepo", "Error HTTP: código $codigoRespuesta")
                return@withContext Result.failure(Exception("Error del servidor: código $codigoRespuesta"))
            }

            val respuesta = conexion.inputStream.bufferedReader().readText()
            if (respuesta.isBlank()) {
                Log.e("EstacionesRepo", "Respuesta vacía")
                return@withContext Result.failure(Exception("Respuesta vacía del servidor"))
            }

            val json = JSONObject(respuesta)
            val features = json.optJSONArray("features")
            if (features == null || features.length() == 0) {
                Log.e("EstacionesRepo", "No se encontró arreglo features")
                return@withContext Result.failure(Exception("No se encontraron estaciones"))
            }

            val estaciones = mutableListOf<EstacionTransmilenio>()
            for (i in 0 until features.length()) {
                try {
                    val feature = features.getJSONObject(i)
                    val attributes = feature.optJSONObject("attributes")
                    val geometry = feature.optJSONObject("geometry")

                    if (attributes == null || geometry == null) continue

                    val nombre = attributes.optString("nom_est").ifBlank { "Estación sin nombre" }
                    val direccion = attributes.optString("ub_est").takeIf { it.isNotBlank() }
                    val tipoEsta = attributes.optInt("tipo_esta", 4)
                    val lng = geometry.optDouble("x", Double.NaN)
                    val lat = geometry.optDouble("y", Double.NaN)

                    if (lng.isNaN() || lat.isNaN()) {
                        Log.w("EstacionesRepo", "Coordenadas inválidas para: $nombre")
                        continue
                    }

                    estaciones.add(
                        EstacionTransmilenio(
                            nomEst = nombre,
                            ubEst = direccion,
                            tipoEsta = tipoEsta,
                            latitud = lat,
                            longitud = lng
                        )
                    )
                } catch (e: Exception) {
                    Log.w("EstacionesRepo", "Error parseando estación $i: ${e.message}")
                }
            }

            Log.d("EstacionesRepo", "Estaciones parseadas: ${estaciones.size}")
            Result.success(estaciones)

        } catch (e: Exception) {
            Log.e("EstacionesRepo", "Error de red: ${e.message}")
            Result.failure(e)
        } finally {
            conexion?.disconnect()
        }
    }
}