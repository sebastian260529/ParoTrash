package com.example.parotrash.navegacion

sealed class Pantallas(val ruta: String) {
    object InicioSesion : Pantallas("inicio_sesion")
    object Registrarse : Pantallas("registrarse")
    object RecuperarContrasena : Pantallas("recuperar_contrasena")
    object CodigoSatisfactorio : Pantallas("codigo_satisfactorio")
}