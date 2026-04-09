package com.example.parotrash.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.parotrash.modelos.Usuario
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class RegistroViewModel : ViewModel() {

    var nombre by mutableStateOf("")
        private set

    var correo by mutableStateOf("")
        private set

    var contraseña by mutableStateOf("")
        private set

    var confirmarContraseña by mutableStateOf("")
        private set


    var cargando by mutableStateOf(false)
        private set

    var errorNombre by mutableStateOf<String?>(null)
        private set

    var errorCorreo by mutableStateOf<String?>(null)
        private set

    var errorContraseña by mutableStateOf<String?>(null)
        private set

    var errorConfirmar by mutableStateOf<String?>(null)
        private set

    var errorGeneral by mutableStateOf<String?>(null)
        private set


    var registroExitoso by mutableStateOf(false)
        private set


    private val auth = FirebaseAuth.getInstance()
    private val firestore = FirebaseFirestore.getInstance()

    fun actualizarNombre(nuevoNombre: String) {
        nombre = nuevoNombre
        errorGeneral = null

        // Validación en tiempo real
        errorNombre = when {
            nuevoNombre.isEmpty() -> "👤 El nombre no puede estar vacío"
            nuevoNombre.length < 3 -> "👤 El nombre debe tener al menos 3 caracteres"
            else -> null
        }
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

        val tieneMayuscula = nuevaContraseña.any { it.isUpperCase() }
        val tieneMinuscula = nuevaContraseña.any { it.isLowerCase() }
        val tieneNumero = nuevaContraseña.any { it.isDigit() }
        val tieneEspecial = nuevaContraseña.any { !it.isLetterOrDigit() }

        errorContraseña = when {
            nuevaContraseña.isEmpty() -> "🔒 La contraseña no puede estar vacía"

            nuevaContraseña.length < 8 ||
                    !tieneMayuscula ||
                    !tieneMinuscula ||
                    !tieneNumero ||
                    !tieneEspecial ->
                "🔐 La contraseña debe tener mínimo 8 caracteres, incluir mayúscula, número y un carácter especial"

            else -> null
        }

        if (confirmarContraseña.isNotEmpty()) {
            actualizarConfirmarContraseña(confirmarContraseña)
        }
    }


    fun actualizarConfirmarContraseña(nuevaConfirmar: String) {
        confirmarContraseña = nuevaConfirmar
        errorGeneral = null

        errorConfirmar = when {
            nuevaConfirmar.isEmpty() -> "🔒 Confirma tu contraseña"
            nuevaConfirmar != contraseña -> "⚠️ Las contraseñas no coinciden"
            else -> null
        }
    }
    fun registrarse() {

        var hayError = false

        if (errorNombre != null) {
            hayError = true
        }
        if (errorCorreo != null) {
            hayError = true
        }
        if (errorContraseña != null) {
            hayError = true
        }
        if (errorConfirmar != null) {
            hayError = true
        }

        if (hayError) {
            return
        }

        cargando = true
        errorGeneral = null

        auth.createUserWithEmailAndPassword(correo, contraseña)
            .addOnCompleteListener { tarea ->
                if (tarea.isSuccessful) {
                    val uid = auth.currentUser?.uid ?: return@addOnCompleteListener

                    val usuario = Usuario(
                        uid = uid,
                        nombre = nombre,
                        correo = correo,
                        reputacion = 0,
                        reportesHechos = 0,
                        fechaRegistro = System.currentTimeMillis()
                    )

                    firestore.collection("usuarios")
                        .document(uid)
                        .set(usuario)
                        .addOnSuccessListener {
                            cargando = false
                            registroExitoso = true
                        }
                        .addOnFailureListener { error ->
                            cargando = false
                            errorGeneral = "❌ Error al guardar datos: ${error.message}"
                        }

                } else {
                    cargando = false
                    val exception = tarea.exception

                    errorGeneral = when (exception) {
                        is com.google.firebase.auth.FirebaseAuthException -> {
                            when (exception.errorCode) {

                                "ERROR_EMAIL_ALREADY_IN_USE",
                                "email-already-in-use",
                                "auth/email-already-in-use" -> "📧 Este correo ya está registrado"

                                // 🚫 Método no habilitado
                                "ERROR_OPERATION_NOT_ALLOWED" -> "🚫 Registro con email no habilitado"

                                else -> "❌ ${exception.localizedMessage ?: "Error al registrarse"}"
                            }
                        }
                        else -> "❌ Error inesperado. Intenta nuevamente"
                    }
                }
            }
    }
}