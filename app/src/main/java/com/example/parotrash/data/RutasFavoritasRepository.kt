package com.example.parotrash.data

import android.util.Log
import com.example.parotrash.modelos.RutaFavorita
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.tasks.await

class RutasFavoritasRepository {

    private val firestore = FirebaseFirestore.getInstance()
    private val coleccion = "rutas_favoritas"

    suspend fun obtenerRutasPorUsuario(uid: String): Result<List<RutaFavorita>> {
        return try {
            val snapshot = firestore.collection(coleccion)
                .whereEqualTo("id_usuario", uid)
                .orderBy("fechaCreacion", Query.Direction.DESCENDING)
                .get()
                .await()

            val rutas = mutableListOf<RutaFavorita>()
            for (doc in snapshot.documents) {
                val ruta = doc.toObject(RutaFavorita::class.java)?.copy(id = doc.id)
                if (ruta != null) {
                    rutas.add(ruta)
                }
            }
            Result.success(rutas)
        } catch (e: Exception) {
            Log.e("RutasFavRepo", "Error obteniendo rutas: ${e.message}")
            Result.failure(e)
        }
    }

    suspend fun guardarRuta(ruta: RutaFavorita): Result<String> {
        return try {
            val docRef = firestore.collection(coleccion).document()
            val nuevaRuta = ruta.copy(id = docRef.id)
            docRef.set(nuevaRuta).await()
            Result.success(docRef.id)
        } catch (e: Exception) {
            Log.e("RutasFavRepo", "Error guardando ruta: ${e.message}")
            Result.failure(e)
        }
    }

    suspend fun actualizarRuta(ruta: RutaFavorita): Result<Unit> {
        return try {
            firestore.collection(coleccion)
                .document(ruta.id)
                .set(ruta)
                .await()
            Result.success(Unit)
        } catch (e: Exception) {
            Log.e("RutasFavRepo", "Error actualizando ruta: ${e.message}")
            Result.failure(e)
        }
    }

    suspend fun eliminarRuta(id: String): Result<Unit> {
        return try {
            firestore.collection(coleccion)
                .document(id)
                .delete()
                .await()
            Result.success(Unit)
        } catch (e: Exception) {
            Log.e("RutasFavRepo", "Error eliminando ruta: ${e.message}")
            Result.failure(e)
        }
    }
}