package com.example.parotrash.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth

class InicioSesionViewModel : ViewModel() {

    private val _correo = MutableLiveData("")
    val correo: LiveData<String> = _correo

    private val _contraseña = MutableLiveData("")
    val contraseña: LiveData<String> = _contraseña

    private val _cargando = MutableLiveData(false)
    val cargando: LiveData<Boolean> = _cargando

    private val _errorCorreo = MutableLiveData<String?>()
    val errorCorreo: LiveData<String?> = _errorCorreo

    private val _errorContraseña = MutableLiveData<String?>()
    val errorContraseña: LiveData<String?> = _errorContraseña

    private val _errorGeneral = MutableLiveData<String?>()
    val errorGeneral: LiveData<String?> = _errorGeneral

    var loginExitoso by mutableStateOf(false)
        private set
    // Actualizar correo
    fun actualizarCorreo(nuevoCorreo: String) {
        _correo.value = nuevoCorreo
        _errorCorreo.value = null
        _errorGeneral.value = null
    }

    fun actualizarContraseña(nuevaContraseña: String) {
        _contraseña.value = nuevaContraseña
        _errorContraseña.value = null
        _errorGeneral.value = null
    }

    // Función de inicio de sesión
    fun iniciarSesion() {
        val correoValor = _correo.value ?: ""
        val contraseñaValor = _contraseña.value ?: ""

        var hayError = false

        when {
            correoValor.isEmpty() -> {
                _errorCorreo.value = "📧 El correo no puede estar vacío"
                hayError = true
            }
            !correoValor.contains("@") -> {
                _errorCorreo.value = "✉️ Ingresa un correo válido (ejemplo@correo.com)"
                hayError = true
            }
            else -> {
                _errorCorreo.value = null
            }
        }


        when {
            contraseñaValor.isEmpty() -> {
                _errorContraseña.value = "🔒 La contraseña no puede estar vacía"
                hayError = true
            }
            contraseñaValor.length < 6 -> {
                _errorContraseña.value = "🔐 La contraseña debe tener al menos 6 caracteres"
                hayError = true
            }
            else -> {
                _errorContraseña.value = null
            }
        }

        if (hayError) {
            return
        }

        // Iniciar sesión con Firebase
        _cargando.value = true
        _errorGeneral.value = null

        FirebaseAuth.getInstance()
            .signInWithEmailAndPassword(correoValor, contraseñaValor)
            .addOnCompleteListener { tarea ->
                _cargando.value = false

                if (tarea.isSuccessful) {
                    loginExitoso = true
                    _errorGeneral.value = null
                } else {
                    val mensajeError = tarea.exception?.message ?: ""
                    _errorGeneral.value = when {
                        mensajeError.contains("user-not-found") -> "👤 Usuario no encontrado"
                        mensajeError.contains("wrong-password") -> "🔑 Contraseña incorrecta"
                        mensajeError.contains("network-error") -> "🌐 Revisa tu conexión a internet"
                        else -> "❌ Error al iniciar sesión. Intenta nuevamente"
                    }
                }
            }
    }

    // Función para invitado
    fun iniciarComoInvitado() {
        _errorGeneral.value = null
        // Aquí va la lógica para invitado
    }
}