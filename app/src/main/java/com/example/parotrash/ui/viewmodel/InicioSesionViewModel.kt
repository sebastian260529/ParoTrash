package com.example.parotrash.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth

class InicioSesionViewModel : ViewModel() {

    var email by mutableStateOf("")
        private set

    var password by mutableStateOf("")
        private set

    var isLoading by mutableStateOf(false)
        private set

    var mensajeError by mutableStateOf<String?>(null)
        private set

    fun updateEmail(nuevoEmail: String) {
        email = nuevoEmail
        mensajeError = null
    }

    fun updatePassword(nuevaPassword: String) {
        password = nuevaPassword
        mensajeError = null
    }

    fun login() {
        when {
            email.isEmpty() -> {
                mensajeError = "El correo no puede estar vacío"
                return
            }
            !email.contains("@") -> {
                mensajeError = "✉Ingresa un correo válido (ejemplo@correo.com)"
                return
            }
            password.isEmpty() -> {
                mensajeError = "La contraseña no puede estar vacía"
                return
            }
            password.length < 6 -> {
                mensajeError = "La contraseña debe tener al menos 6 caracteres"
                return
            }
        }

        isLoading = true
        mensajeError = null

        FirebaseAuth.getInstance()
            .signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                isLoading = false
                if (!task.isSuccessful) {
                    mensajeError = when {
                        task.exception?.message?.contains("user-not-found") == true -> "👤 Usuario no encontrado"
                        task.exception?.message?.contains("wrong-password") == true -> "🔑 Contraseña incorrecta"
                        else -> "❌ Error al iniciar sesión: ${task.exception?.message}"
                    }
                }
            }
    }
    fun recuperarPassword() {
        if (email.isEmpty()) {
            mensajeError = "✉️ Ingresa tu correo para enviarte el enlace"
            return
        }
        if (!email.contains("@")) {
            mensajeError = "✉️ Ingresa un correo válido"
            return
        }

        isLoading = true
        mensajeError = null

        FirebaseAuth.getInstance().sendPasswordResetEmail(email)
            .addOnCompleteListener { task ->
                isLoading = false
                if (task.isSuccessful) {
                    mensajeError = "✅ Enlace enviado. Revisa tu correo (spam incluido)"
                } else {
                    mensajeError = "❌ Error: ${task.exception?.message}"
                }
            }
    }

}