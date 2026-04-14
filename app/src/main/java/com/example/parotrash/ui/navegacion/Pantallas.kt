package com.example.parotrash.ui.navegacion

sealed class Pantallas(val ruta: String) {
    object Bienvenida : Pantallas("bienvenida")
    object InicioSesion : Pantallas("inicio_sesion")
    object Registrarse : Pantallas("registrarse")
    object RecuperarContrasena : Pantallas("recuperar_contrasena")
    object CodigoSatisfactorio : Pantallas("codigo_satisfactorio")
    object RegistroExitoso : Pantallas("registro_exitoso")
    object Home : Pantallas("home")
    object Configuracion : Pantallas("configuracion")
    object MiCuenta : Pantallas("mi_cuenta")
    object CambiarUsuario : Pantallas("cambiar_usuario")
    object CambiarCorreo : Pantallas("cambiar_correo")
    object CambiarContrasena : Pantallas("cambiar_contrasena")
    object Notificaciones : Pantallas("notificaciones")
    object Privacidad : Pantallas("privacidad")
}