package com.example.parotrash.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.parotrash.data.SessionManager
import com.example.parotrash.modelos.Usuario
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class InicioSesionViewModel : ViewModel() {

    private val auth = FirebaseAuth.getInstance()
    private val firestore = FirebaseFirestore.getInstance()

    var correo by mutableStateOf("")
        private set

    var contraseña by mutableStateOf("")
        private set

    var cargando by mutableStateOf(false)
        private set

    var cargandoInvitado by mutableStateOf(false)
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
        cargandoInvitado = false
        errorCorreo = null
        errorContraseña = null
        errorGeneral = null
        loginExitoso = false
    }

    fun actualizarCorreo(nuevoCorreo: String) {
        correo = nuevoCorreo
        errorGeneral = null

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

        auth.signInWithEmailAndPassword(correo, contraseña)
            .addOnCompleteListener { tarea ->
                cargando = false
                if (tarea.isSuccessful) {
                    loginExitoso = true
                } else {
                    val exception = tarea.exception
                    errorGeneral = when (exception) {
                        is com.google.firebase.auth.FirebaseAuthInvalidCredentialsException -> "❌ Correo o contraseña incorrectos"
                        else -> "❌ Error al iniciar sesión"
                    }
                }
            }
    }

    fun iniciarComoInvitado() {
        viewModelScope.launch {
            cargandoInvitado = true
            errorGeneral = null
            try {
                // 1. Autenticación Anónima en Firebase
                val authResult = auth.signInAnonymously().await()
                val uid = authResult.user?.uid ?: throw Exception("No se pudo obtener el UID")

                // 2. Crear documento de usuario invitado en Firestore
                val usuarioInvitado = Usuario(
                    uid = uid,
                    nombre = "Invitado",
                    correo = "anonimo@parotrash.com",
                    reputacion = 0,
                    reportesHechos = 0,
                    fechaRegistro = System.currentTimeMillis()
                )

                firestore.collection("usuarios")
                    .document(uid)
                    .set(usuarioInvitado)
                    .await()

                loginExitoso = true
            } catch (e: Exception) {
                errorGeneral = "❌ Error al entrar como invitado: ${e.message}"
            } finally {
                cargandoInvitado = false
            }
        }
    }

    fun cerrarSesion(sessionManager: SessionManager, onCerrarSesion: () -> Unit) {
        viewModelScope.launch {
            sessionManager.logout()
            loginExitoso = false
            onCerrarSesion()
        }
    }
}
