package com.example.parotrash.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.parotrash.data.SessionManager
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch

class InicioSesionViewModel : ViewModel() {


    var correo by mutableStateOf("")
        private set

    var contraseña by mutableStateOf("")
        private set


    var cargando by mutableStateOf(false)
        private set

    var errorCorreo by mutableStateOf<String?>(null)
        private set

    var errorContraseña by mutableStateOf<String?>(null)
        private set

    var errorGeneral by mutableStateOf<String?>(null)
        private set


    var loginExitoso by mutableStateOf(false)

    fun resetearEstado() {
        correo = ""
        contraseña = ""
        cargando = false
        errorCorreo = null
        errorContraseña = null
        errorGeneral = null
        loginExitoso = false
    }
    fun actualizarCorreo(nuevoCorreo: String) {
        correo = nuevoCorreo
        errorGeneral = null

        // Validación en tiempo real
        errorCorreo = when {
            nuevoCorreo.isEmpty() -> "📧 El correo no puede estar vacío"
            !nuevoCorreo.contains("@") -> "✉️ Ingresa un correo válido (ejemplo@correo.com)"
            else -> null
        }
    }


    fun actualizarContraseña(nuevaContraseña: String) {
        contraseña = nuevaContraseña
        errorGeneral = null


        errorContraseña = when {
            nuevaContraseña.isEmpty() -> "🔒 La contraseña no puede estar vacía"
            nuevaContraseña.length < 6 -> "🔐 La contraseña debe tener al menos 8 caracteres"
            else -> null
        }
    }


    fun iniciarSesion() {
        var hayError = false


        when {
            correo.isEmpty() -> {
                errorCorreo = "📧 El correo no puede estar vacío"
                hayError = true
            }
            else -> errorCorreo = null
        }

        when {
            contraseña.isEmpty() -> {
                errorContraseña = "🔒 La contraseña no puede estar vacía"
                hayError = true
            }
            else -> errorContraseña = null
        }

        if (hayError) {
            errorGeneral = "❌ Completa todos los campos correctamente"
            return
        }
        cargando = true
        errorGeneral = null



        FirebaseAuth.getInstance()
            .signInWithEmailAndPassword(correo, contraseña)
            .addOnCompleteListener { tarea ->
                cargando = false

                if (tarea.isSuccessful) {
                    loginExitoso = true
                    errorGeneral = null
                }
                else {
                    cargando = false
                    val exception = tarea.exception

                    errorGeneral = when (exception) {

                        is com.google.firebase.auth.FirebaseAuthInvalidCredentialsException -> {
                            when (exception.errorCode) {
                                "ERROR_INVALID_EMAIL" -> "✉️ Correo electrónico inválido"
                                "ERROR_WRONG_PASSWORD" -> "🔑 Contraseña incorrecta"
                                else -> "❌ Correo o contraseña incorrectos"
                            }
                        }

                        is com.google.firebase.auth.FirebaseAuthException -> {
                            when (exception.errorCode) {

                                "ERROR_TOO_MANY_REQUESTS" ->
                                    "⏳ Demasiados intentos. Intenta más tarde"

                                "ERROR_OPERATION_NOT_ALLOWED" ->
                                    "🚫 Inicio de sesión no habilitado"

                                "ERROR_INVALID_CREDENTIAL",
                                "auth/invalid-credential" ->
                                    "❌ Credenciales incorrectas"

                                else -> "❌ ${exception.localizedMessage ?: "Error al iniciar sesión"}"
                            }
                        }

                        else -> "❌ Error inesperado. Intenta nuevamente"
                    }
                }
            }
    }


    fun iniciarComoInvitado() {
        errorGeneral = null
        loginExitoso = true
    }
    fun cerrarSesion(sessionManager: SessionManager, onCerrarSesion: () -> Unit) {
        viewModelScope.launch {
            sessionManager.logout()
            loginExitoso = false
            onCerrarSesion()
        }
    }
}