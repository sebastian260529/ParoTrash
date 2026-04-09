package com.example.parotrash.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.parotrash.modelos.Usuario
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class RegistroViewModel : ViewModel() {

    // Estados para los campos (mutableStateOf)
    var nombre by mutableStateOf("")
        private set

    var correo by mutableStateOf("")
        private set

    var contraseña by mutableStateOf("")
        private set

    var confirmarContraseña by mutableStateOf("")
        private set

    // Estado de carga
    var cargando by mutableStateOf(false)
        private set

    // Errores por campo (en tiempo real)
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

    // Estado para navegación
    var registroExitoso by mutableStateOf(false)
        private set

    // Firebase instances
    private val auth = FirebaseAuth.getInstance()
    private val firestore = FirebaseFirestore.getInstance()

    // Actualizar nombre (con validación en tiempo real)
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

    // Actualizar correo (con validación en tiempo real)
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

    // Actualizar contraseña (con validación en tiempo real)
    fun actualizarContraseña(nuevaContraseña: String) {
        contraseña = nuevaContraseña
        errorGeneral = null

        // Validación en tiempo real
        errorContraseña = when {
            nuevaContraseña.isEmpty() -> "🔒 La contraseña no puede estar vacía"
            nuevaContraseña.length < 6 -> "🔐 La contraseña debe tener al menos 6 caracteres"
            else -> null
        }

        // Re-validar confirmar contraseña si ya tiene valor
        if (confirmarContraseña.isNotEmpty()) {
            actualizarConfirmarContraseña(confirmarContraseña)
        }
    }

    // Actualizar confirmar contraseña (con validación en tiempo real)
    fun actualizarConfirmarContraseña(nuevaConfirmar: String) {
        confirmarContraseña = nuevaConfirmar
        errorGeneral = null

        // Validación en tiempo real
        errorConfirmar = when {
            nuevaConfirmar.isEmpty() -> "🔒 Confirma tu contraseña"
            nuevaConfirmar != contraseña -> "⚠️ Las contraseñas no coinciden"
            else -> null
        }
    }

    // Función de registro
    fun registrarse() {
        // Validaciones finales (por si acaso)
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

        // Registrar en Firebase Authentication
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
                    val mensajeError = tarea.exception?.message ?: ""
                    errorGeneral = when {
                        mensajeError.contains("email-already-in-use") -> "📧 Este correo ya está registrado"
                        mensajeError.contains("invalid-email") -> "✉️ Correo electrónico inválido"
                        mensajeError.contains("weak-password") -> "🔐 La contraseña es muy débil"
                        else -> "❌ Error al registrarse. Intenta nuevamente"
                    }
                }
            }
    }
}