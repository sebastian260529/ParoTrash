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
        if (uid.isBlank()) {
            return Result.failure(IllegalArgumentException("ID de usuario inválido"))
        }
        
        return try {
            val snapshot = firestore.collection(coleccion)
                .whereEqualTo("id_usuario", uid)
                .orderBy("fechaCreacion", Query.Direction.DESCENDING)
                .get()
                .await()

            if (snapshot.isEmpty()) {
                Log.d("RutasFavRepo", "No hay rutas para usuario: $uid")
                return Result.success(emptyList())
            }

            val rutas = mutableListOf<RutaFavorita>()
            for (doc in snapshot.documents) {
                try {
                    val ruta = doc.toObject(RutaFavorita::class.java)?.copy(id = doc.id)
                    if (ruta != null && ruta.nombre.isNotBlank()) {
                        rutas.add(ruta)
                    }
                } catch (e: Exception) {
                    Log.w("RutasFavRepo", "Error parseando documento ${doc.id}: ${e.message}")
                }
            }
            Result.success(rutas)
        } catch (e: com.google.firebase.FirebaseException) {
            Log.e("RutasFavRepo", "Firebase error: ${e.message}")
            Result.failure(Exception("Error de Firebase: ${e.message}"))
        } catch (e: kotlinx.coroutines.TimeoutCancellationException) {
            Log.e("RutasFavRepo", "Timeout: ${e.message}")
            Result.failure(Exception("Tiempo de conexión agotado"))
        } catch (e: Exception) {
            Log.e("RutasFavRepo", "Error obteniendo rutas: ${e.message}")
            Result.failure(Exception("Error al obtener rutas: ${e.message}"))
        }
    }

    suspend fun guardarRuta(ruta: RutaFavorita): Result<String> {
        if (ruta.id_usuario.isBlank()) {
            return Result.failure(IllegalArgumentException("Usuario no autenticado"))
        }
        if (ruta.nombre.isBlank()) {
            return Result.failure(IllegalArgumentException("El nombre es obligatorio"))
        }
        
        return try {
            val docRef = firestore.collection(coleccion).document()
            val nuevaRuta = ruta.copy(id = docRef.id)
            docRef.set(nuevaRuta).await()
            Log.d("RutasFavRepo", "Ruta guardada: ${docRef.id}")
            Result.success(docRef.id)
        } catch (e: com.google.firebase.FirebaseException) {
            Log.e("RutasFavRepo", "Firebase error: ${e.message}")
            Result.failure(Exception("Error al guardar en Firebase"))
        } catch (e: Exception) {
            Log.e("RutasFavRepo", "Error guardando ruta: ${e.message}")
            Result.failure(Exception("Error al guardar ruta: ${e.message}"))
        }
    }

    suspend fun actualizarRuta(ruta: RutaFavorita): Result<Unit> {
        if (ruta.id.isBlank()) {
            return Result.failure(IllegalArgumentException("ID de ruta inválido"))
        }
        
        return try {
            firestore.collection(coleccion)
                .document(ruta.id)
                .set(ruta)
                .await()
            Result.success(Unit)
        } catch (e: Exception) {
            Log.e("RutasFavRepo", "Error actualizando ruta: ${e.message}")
            Result.failure(Exception("Error al actualizar: ${e.message}"))
        }
    }

    suspend fun eliminarRuta(id: String): Result<Unit> {
        if (id.isBlank()) {
            return Result.failure(IllegalArgumentException("ID de ruta inválido"))
        }
        
        return try {
            firestore.collection(coleccion)
                .document(id)
                .delete()
                .await()
            Result.success(Unit)
        } catch (e: Exception) {
            Log.e("RutasFavRepo", "Error eliminando ruta: ${e.message}")
            Result.failure(Exception("Error al eliminar: ${e.message}"))
        }
    }
}