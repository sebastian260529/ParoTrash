package com.example.parotrash.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class CambiarUsuarioViewModel : ViewModel() {

    private val auth = FirebaseAuth.getInstance()
    private val db = FirebaseFirestore.getInstance()

    var usuarioActual by mutableStateOf("")
        private set

    var nuevoUsuario by mutableStateOf("")
        private set

    var error by mutableStateOf("")
        private set

    var isLoading by mutableStateOf(false)
        private set

    var exito by mutableStateOf(false)
        private set

    init {
        cargarUsuarioActual()
    }

    // 🔹 Obtener usuario actual desde Firestore
    private fun cargarUsuarioActual() {
        val uid = auth.currentUser?.uid ?: return

        db.collection("usuarios")
            .document(uid)
            .get()
            .addOnSuccessListener { doc ->
                usuarioActual = doc.getString("usuario") ?: ""
            }
            .addOnFailureListener {
                error = "Error al cargar usuario"
            }
    }

    // 🔹 setter
    fun onNuevoUsuarioChange(valor: String) {
        nuevoUsuario = valor
    }

    // 🔹 validación
    private fun validar(): Boolean {
        return when {
            nuevoUsuario.length < 3 -> {
                error = "Mínimo 3 caracteres"
                false
            }
            nuevoUsuario.length > 30 -> {
                error = "Máximo 30 caracteres"
                false
            }
            nuevoUsuario == usuarioActual -> {
                error = "El usuario es el mismo"
                false
            }
            else -> {
                error = ""
                true
            }
        }
    }

    // 🔥 FUNCIÓN PRINCIPAL
    fun cambiarUsuario(onSuccess: () -> Unit) {

        val uid = auth.currentUser?.uid

        if (uid == null) {
            error = "Usuario no autenticado"
            return
        }

        if (!validar()) return

        isLoading = true

        // 🔍 Verificar si el usuario ya existe
        db.collection("usuarios")
            .whereEqualTo("usuario", nuevoUsuario)
            .get()
            .addOnSuccessListener { query ->

                if (!query.isEmpty) {
                    isLoading = false
                    error = "El usuario ya está en uso"
                    return@addOnSuccessListener
                }

                // 💾 Actualizar usuario
                db.collection("usuarios")
                    .document(uid)
                    .update("usuario", nuevoUsuario)
                    .addOnSuccessListener {
                        isLoading = false
                        exito = true
                        usuarioActual = nuevoUsuario
                        nuevoUsuario = ""
                        error = ""
                        onSuccess()
                    }
                    .addOnFailureListener {
                        isLoading = false
                        error = "Error al actualizar usuario"
                    }
            }
            .addOnFailureListener {
                isLoading = false
                error = "Error al verificar usuario"
            }
    }

    fun limpiar() {
        nuevoUsuario = ""
        error = ""
        exito = false
    }
}