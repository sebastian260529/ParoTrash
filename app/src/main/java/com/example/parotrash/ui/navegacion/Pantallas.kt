package com.example.parotrash.ui.navegacion

sealed class Pantallas(val ruta: String) {
    object Bienvenida : Pantallas("bienvenida")
    object InicioSesion : Pantallas("inicio_sesion")
    object Registrarse : Pantallas("registrarse")
    object RecuperarContrasena : Pantallas("recuperar_contrasena")
    object CodigoSatisfactorio : Pantallas("codigo_satisfactorio")
    object RegistroExitoso : Pantallas("registro_exitoso")
    object InicioSesionFallido : Pantallas("inicio_sesion_fallido")
}