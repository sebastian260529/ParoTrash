package com.example.parotrash.ui.viewmodel

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.parotrash.modelos.Usuario
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration

class UsuarioViewModel : ViewModel() {

    private val auth = FirebaseAuth.getInstance()
    private val firestore = FirebaseFirestore.getInstance()
    private var usuarioListener: ListenerRegistration? = null

    var datosUsuario by mutableStateOf<Usuario?>(null)
        private set

    init {
        // Escuchar cambios en la autenticación para cargar datos automáticamente
        auth.addAuthStateListener { firebaseAuth ->
            val uid = firebaseAuth.currentUser?.uid
            if (uid != null) {
                cargarDatosUsuario(uid)
            } else {
                usuarioListener?.remove()
                datosUsuario = null
            }
        }
    }

    private fun cargarDatosUsuario(uid: String) {
        usuarioListener?.remove() // Evitar múltiples listeners
        usuarioListener = firestore.collection("usuarios").document(uid)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    Log.e("UsuarioViewModel", "Error al escuchar usuario: ${error.message}")
                    return@addSnapshotListener
                }
                if (snapshot != null && snapshot.exists()) {
                    datosUsuario = snapshot.toObject(Usuario::class.java)
                }
            }
    }

    /**
     * Actualiza la reputación y el contador de reportes.
     * Se usa una transacción para asegurar consistencia.
     */
    fun registrarNuevoReporte() {
        val uid = auth.currentUser?.uid ?: run {
            Log.e("UsuarioViewModel", "No se puede actualizar: UID es nulo")
            return
        }
        val userRef = firestore.collection("usuarios").document(uid)

        firestore.runTransaction { transaction ->
            val snapshot = transaction.get(userRef)
            
            // Si el documento no existe, lo intentamos crear (caso borde de invitados lentos)
            if (!snapshot.exists()) {
                val nuevo = Usuario(uid = uid, nombre = "Invitado", reputacion = 15, reportesHechos = 1)
                transaction.set(userRef, nuevo)
            } else {
                val usuarioActual = snapshot.toObject(Usuario::class.java)
                if (usuarioActual != null) {
                    val nuevosReportes = usuarioActual.reportesHechos + 1
                    // Bonus: 15 puntos base + 5 extra cada 10 reportes
                    val puntosGanados = 15 + (nuevosReportes / 10 * 5)
                    val nuevaReputacion = usuarioActual.reputacion + puntosGanados

                    transaction.update(userRef, "reportesHechos", nuevosReportes)
                    transaction.update(userRef, "reputacion", nuevaReputacion)
                }
            }
        }.addOnSuccessListener {
            Log.d("UsuarioViewModel", "Estadísticas actualizadas en Firebase")
        }.addOnFailureListener { e ->
            Log.e("UsuarioViewModel", "Fallo en transacción: ${e.message}")
        }
    }

    fun recibirConfirmacion(puntos: Int = 5) {
        val uid = auth.currentUser?.uid ?: return
        val userRef = firestore.collection("usuarios").document(uid)
        
        firestore.runTransaction { transaction ->
            val snapshot = transaction.get(userRef)
            if (snapshot.exists()) {
                val reputacionActual = snapshot.getLong("reputacion") ?: 0L
                transaction.update(userRef, "reputacion", reputacionActual + puntos)
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        usuarioListener?.remove()
    }
}
