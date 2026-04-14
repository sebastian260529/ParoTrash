package com.example.parotrash.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth

class CambiarCorreoViewModel : ViewModel() {

    private val auth = FirebaseAuth.getInstance()

    var correoActual by mutableStateOf("")
        private set

    var nuevoCorreo by mutableStateOf("")
        private set

    var contrasena by mutableStateOf("")
        private set

    var error by mutableStateOf("")
        private set

    var isLoading by mutableStateOf(false)
        private set

    var exito by mutableStateOf(false)
        private set

    init {
        correoActual = auth.currentUser?.email ?: ""
    }

    // 🔹 setters
    fun onNuevoCorreoChange(valor: String) {
        nuevoCorreo = valor
    }

    fun onContrasenaChange(valor: String) {
        contrasena = valor
    }

    // 🔹 validación
    private fun validar(): Boolean {
        return when {
            nuevoCorreo.isEmpty() -> {
                error = "Ingrese un nuevo correo"
                false
            }
            !android.util.Patterns.EMAIL_ADDRESS.matcher(nuevoCorreo).matches() -> {
                error = "Correo inválido"
                false
            }
            contrasena.isEmpty() -> {
                error = "Ingrese su contraseña"
                false
            }
            else -> {
                error = ""
                true
            }
        }
    }

    // 🔥 FUNCIÓN PRINCIPAL
    fun cambiarCorreo(onSuccess: () -> Unit) {

        val user = auth.currentUser

        if (user == null || user.email == null) {
            error = "Usuario no autenticado"
            return
        }

        if (!validar()) return

        isLoading = true

        val credential = EmailAuthProvider.getCredential(
            user.email!!,
            contrasena
        )

        // 🔐 1. Reautenticación
        user.reauthenticate(credential)
            .addOnSuccessListener {

                // 📧 2. Cambiar correo
                user.updateEmail(nuevoCorreo)
                    .addOnSuccessListener {
                        isLoading = false
                        exito = true
                        error = ""
                        correoActual = nuevoCorreo
                        nuevoCorreo = ""
                        contrasena = ""
                        onSuccess()
                    }
                    .addOnFailureListener { e ->
                        isLoading = false
                        error = manejarError(e.message)
                    }

            }
            .addOnFailureListener { e ->
                isLoading = false
                error = when {
                    e.message?.contains("password is invalid") == true ->
                        "Contraseña incorrecta"

                    e.message?.contains("recent login required") == true ->
                        "Vuelve a iniciar sesión"

                    else -> "Error al verificar credenciales"
                }
            }
    }

    fun limpiar() {
        nuevoCorreo = ""
        contrasena = ""
        error = ""
        exito = false
    }

    // 🔧 manejo de errores
    private fun manejarError(mensaje: String?): String {
        return when {
            mensaje?.contains("email address is already in use") == true ->
                "El correo ya está en uso"

            mensaje?.contains("badly formatted") == true ->
                "Correo inválido"

            mensaje?.contains("network error") == true ->
                "Error de conexión"

            else -> "Error al cambiar el correo"
        }
    }
}