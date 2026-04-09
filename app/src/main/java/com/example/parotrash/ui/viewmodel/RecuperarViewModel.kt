package com.example.parotrash.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth

class RecuperarViewModel : ViewModel() {

    // Estados
    var correo by mutableStateOf("")
        private set

    var cargando by mutableStateOf(false)
        private set

    var mensaje by mutableStateOf<String?>(null)
        private set

    var correoEnviado by mutableStateOf(false)
        private set


    private val auth = FirebaseAuth.getInstance()

    fun actualizarCorreo(nuevoCorreo: String) {
        correo = nuevoCorreo
        correoEnviado = false
        mensaje = when {
            nuevoCorreo.isEmpty() -> "📧 Ingresa tu correo electrónico"
            !nuevoCorreo.contains("@") -> "✉️ Ingresa un correo válido (ejemplo@correo.com)"
            else -> null
        }
    }


    fun recuperarContraseña() {
        if (correo.isEmpty()) {
            mensaje = "📧 Ingresa tu correo electrónico"
            return
        }
        if (!correo.contains("@")) {
            mensaje = "✉️ Ingresa un correo válido (ejemplo@correo.com)"
            return
        }

        if (mensaje != null) {
            return
        }

        cargando = true
        mensaje = null
        correoEnviado = false

        auth.sendPasswordResetEmail(correo)
            .addOnCompleteListener { tarea ->
                cargando = false

                if (tarea.isSuccessful) {
                    mensaje = "📧 Se envió un correo a $correo. Revisa tu bandeja de entrada."
                    correoEnviado = true  // ← Activar navegación
                } else {
                    val exception = tarea.exception

                    mensaje = when (exception) {

                        is com.google.firebase.auth.FirebaseAuthInvalidCredentialsException -> {
                            when (exception.errorCode) {
                                "ERROR_INVALID_EMAIL" -> "✉️ Correo electrónico no encontrado"
                                else -> "❌ Correo inválido"
                            }
                        }

                        is com.google.firebase.auth.FirebaseAuthException -> {
                            when (exception.errorCode) {

                                "ERROR_MISSING_EMAIL" ->
                                    "⚠️ Ingresa un correo electrónico"

                                "ERROR_NETWORK_REQUEST_FAILED" ->
                                    "🌐 Revisa tu conexión a internet"

                                else -> "❌ No se pudo enviar el correo"
                            }
                        }

                        else -> "❌ Error inesperado"
                    }
                }
            }
    }

}