package com.example.parotrash.ui.navegacion

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.parotrash.data.SessionManager
import com.example.parotrash.ui.pantallas.PantallaBienvenida
import com.example.parotrash.ui.pantallas.PantallaCodigoSatisfactorio
import com.example.parotrash.ui.pantallas.PantallaHome
import com.example.parotrash.ui.pantallas.PantallaInicioSesion
import com.example.parotrash.ui.pantallas.PantallaRecuperarContraseña
import com.example.parotrash.ui.pantallas.PantallaRegistrarse
import com.example.parotrash.ui.pantallas.PantallaRegistroExitoso
import com.example.parotrash.ui.viewmodel.InicioSesionViewModel
import com.example.parotrash.ui.viewmodel.RecuperarViewModel
import com.example.parotrash.ui.viewmodel.RegistroViewModel

@Composable
fun NavegacionApp(
    navController: NavHostController,
    startDestination: String,
    sessionManager: SessionManager

) {
    val loginViewModel: InicioSesionViewModel = viewModel()
    val registroViewModel: RegistroViewModel = viewModel()
    val recuperarViewModel: RecuperarViewModel = viewModel()

    NavHost(
        navController = navController,
        startDestination = startDestination,

    ) {
        composable(Pantallas.Bienvenida.ruta) {
        PantallaBienvenida(
            irAInicioSesion = {
                navController.navigate(Pantallas.InicioSesion.ruta)
            },
            sessionManager = sessionManager
        )
    }

        composable(Pantallas.InicioSesion.ruta) {
            PantallaInicioSesion(
                viewModel = loginViewModel,
                irARegistro = {
                    navController.navigate(Pantallas.Registrarse.ruta)
                },
                irARecuperar = {
                    navController.navigate(Pantallas.RecuperarContrasena.ruta)
                },
                irAHome = {
                    navController.navigate(Pantallas.Home.ruta) {
                        popUpTo(Pantallas.InicioSesion.ruta) {
                            inclusive = true
                        }
                    }
                },
                sessionManager = sessionManager
            )
        }

        composable(Pantallas.Registrarse.ruta) {
            PantallaRegistrarse(
                viewModel = registroViewModel,
                irAInicio = {
                    navController.popBackStack()
                },
                irARegistroExitoso = {
                    navController.navigate(Pantallas.RegistroExitoso.ruta) {
                        popUpTo(Pantallas.Registrarse.ruta) {
                            inclusive = true
                        }
                    }
                }
            )
        }

        composable(Pantallas.RecuperarContrasena.ruta) {
            PantallaRecuperarContraseña(
                viewModel = recuperarViewModel,
                irARegistro= {
                    navController.navigate(Pantallas.Registrarse.ruta)
                },
                irACodigo = {
                    navController.navigate(Pantallas.CodigoSatisfactorio.ruta)
                }
            )
        }

        // Pantalla de Código Enviado (éxito al recuperar)
        composable(Pantallas.CodigoSatisfactorio.ruta) {
            PantallaCodigoSatisfactorio(
                irAInicioSesion = {
                    navController.navigate(Pantallas.InicioSesion.ruta) {
                        popUpTo(Pantallas.CodigoSatisfactorio.ruta) {
                            inclusive = true
                        }
                    }
                }
            )
        }
        // Pantalla de Registro Exitoso
        composable(Pantallas.RegistroExitoso.ruta) {
            PantallaRegistroExitoso(
                irAInicioSesion = {
                    navController.navigate(Pantallas.InicioSesion.ruta) {
                        popUpTo(Pantallas.RegistroExitoso.ruta) {
                            inclusive = true
                        }
                    }
                }
            )
        }

        composable(Pantallas.Home.ruta) {
            PantallaHome(
                viewModel = loginViewModel,
                irACerrarSesion = {
                    navController.navigate(Pantallas.InicioSesion.ruta) {
                        popUpTo(Pantallas.Home.ruta) {
                            inclusive = true
                        }
                    }
                },
                sessionManager = sessionManager
            )
        }

    }
}