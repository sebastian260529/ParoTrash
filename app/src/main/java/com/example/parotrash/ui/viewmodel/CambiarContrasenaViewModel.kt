package com.example.parotrash.ui.viewmodel


import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth

class CambiarContrasenaViewModel : ViewModel() {

    private val auth = FirebaseAuth.getInstance()

    var contrasenaActual by mutableStateOf("")
        private set

    var nuevaContrasena by mutableStateOf("")
        private set

    var confirmarContrasena by mutableStateOf("")
        private set

    var error by mutableStateOf("")
        private set

    var isLoading by mutableStateOf(false)
        private set

    var exito by mutableStateOf(false)
        private set

    // 🔹 setters
    fun onContrasenaActualChange(valor: String) {
        contrasenaActual = valor
    }

    fun onNuevaContrasenaChange(valor: String) {
        nuevaContrasena = valor
    }

    fun onConfirmarContrasenaChange(valor: String) {
        confirmarContrasena = valor
    }

    // 🔹 validación
    private fun validar(): Boolean {
        return when {
            contrasenaActual.isEmpty() -> {
                error = "Ingrese la contraseña actual"
                false
            }
            nuevaContrasena.length < 8 -> {
                error = "La nueva contraseña debe tener al menos 8 caracteres"
                false
            }
            nuevaContrasena != confirmarContrasena -> {
                error = "Las contraseñas no coinciden"
                false
            }
            else -> {
                error = ""
                true
            }
        }
    }

    // 🔥 FUNCIÓN PRINCIPAL
    fun cambiarContrasena(onSuccess: () -> Unit) {

        val user = auth.currentUser

        if (user == null || user.email == null) {
            error = "Usuario no autenticado"
            return
        }

        if (!validar()) return

        isLoading = true

        // 🔐 1. Reautenticación
        val credential = EmailAuthProvider.getCredential(
            user.email!!,
            contrasenaActual
        )

        user.reauthenticate(credential)
            .addOnSuccessListener {

                // 🔑 2. Cambiar contraseña
                user.updatePassword(nuevaContrasena)
                    .addOnSuccessListener {
                        isLoading = false
                        exito = true
                        error = ""
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
                        "Contraseña actual incorrecta"

                    e.message?.contains("recent login required") == true ->
                        "Debes iniciar sesión nuevamente"

                    else -> "Error al verificar credenciales"
                }
            }
    }

    // 🔧 limpiar campos
    fun limpiar() {
        contrasenaActual = ""
        nuevaContrasena = ""
        confirmarContrasena = ""
        error = ""
        exito = false
    }

    // 🔹 manejo de errores más limpio
    private fun manejarError(mensaje: String?): String {
        return when {
            mensaje?.contains("weak-password") == true ->
                "La contraseña es demasiado débil"

            mensaje?.contains("network error") == true ->
                "Error de conexión"

            mensaje?.contains("recent login required") == true ->
                "Vuelve a iniciar sesión"

            else -> "Error al cambiar la contraseña"
        }
    }
}