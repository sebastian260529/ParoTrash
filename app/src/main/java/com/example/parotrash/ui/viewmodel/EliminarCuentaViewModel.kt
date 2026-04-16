package com.example.parotrash.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.parotrash.data.SessionManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.launch

class EliminarCuentaViewModel : ViewModel() {

    private val auth = FirebaseAuth.getInstance()
    private val firestore = FirebaseFirestore.getInstance()

    var contrasena by mutableStateOf("")
        private set
    var errorContrasena by mutableStateOf<String?>(null)
        private set
    var errorGeneral by mutableStateOf<String?>(null)
        private set
    var isLoading by mutableStateOf(false)
        private set
    var verificacionExitosa by mutableStateOf(false)
        private set

    fun actualizarContrasena(nueva: String) {
        contrasena = nueva
        errorContrasena = when {
            nueva.isEmpty() -> "🔒 Ingresa tu contraseña actual"
            else -> null
        }
        errorGeneral = null
    }

    fun verificarContrasena(onResult: (Boolean) -> Unit) {
        val user = auth.currentUser
        if (user == null) {
            errorGeneral = "No hay usuario autenticado"
            onResult(false)
            return
        }
        val email = user.email ?: run {
            errorGeneral = "Correo no disponible"
            onResult(false)
            return
        }

        if (contrasena.isEmpty()) {
            errorContrasena = "🔒 Ingresa tu contraseña actual"
            onResult(false)
            return
        }

        isLoading = true
        val credential = EmailAuthProvider.getCredential(email, contrasena)
        user.reauthenticate(credential)
            .addOnCompleteListener { task ->
                isLoading = false
                if (task.isSuccessful) {
                    verificacionExitosa = true
                    onResult(true)
                } else {
                    errorContrasena = when (task.exception) {
                        is com.google.firebase.auth.FirebaseAuthInvalidCredentialsException ->
                            "🔑 Contraseña incorrecta"
                        else -> "❌ Error al verificar: ${task.exception?.message}"
                    }
                    onResult(false)
                }
            }
    }

    fun eliminarCuenta(
        sessionManager: SessionManager,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        val user = auth.currentUser ?: run {
            onError("No hay usuario autenticado")
            return
        }
        val uid = user.uid

        isLoading = true
        firestore.collection("usuarios").document(uid)
            .delete()
            .addOnSuccessListener {
                user.delete()
                    .addOnCompleteListener { task ->
                        isLoading = false
                        if (task.isSuccessful) {
                            viewModelScope.launch {
                                sessionManager.logout()
                                onSuccess()
                            }
                        } else {
                            onError(task.exception?.message ?: "Error al eliminar autenticación")
                        }
                    }
            }
            .addOnFailureListener { e ->
                isLoading = false
                onError(e.message ?: "Error al eliminar datos")
            }
    }
}