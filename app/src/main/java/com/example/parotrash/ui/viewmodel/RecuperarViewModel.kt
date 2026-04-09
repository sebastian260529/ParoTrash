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

    // Estado para navegación (AGREGAR)
    var correoEnviado by mutableStateOf(false)
        private set

    // Firebase Auth
    private val auth = FirebaseAuth.getInstance()

    // Actualizar correo
    fun actualizarCorreo(nuevoCorreo: String) {
        correo = nuevoCorreo
        mensaje = null
        correoEnviado = false  // Resetear al escribir
    }

    // Función para recuperar contraseña
    fun recuperarContraseña() {
        // Validar correo
        when {
            correo.isEmpty() -> {
                mensaje = "📧 Ingresa tu correo electrónico"
                return
            }
            !correo.contains("@") -> {
                mensaje = "✉️ Ingresa un correo válido (ejemplo@correo.com)"
                return
            }
        }

        // Enviar correo de recuperación
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
                    val errorReal = tarea.exception?.message ?: "Error desconocido"
                    mensaje = when {
                        errorReal.contains("user-not-found") -> "👤 No existe una cuenta con este correo"
                        errorReal.contains("network") -> "🌐 Revisa tu conexión a internet"
                        else -> "❌ Error: No se pudo enviar el correo"
                    }
                }
            }
    }

    // Limpiar mensaje
    fun limpiarMensaje() {
        mensaje = null
        correoEnviado = false
    }
}