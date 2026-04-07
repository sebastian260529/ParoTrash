package com.example.parotrash.navegacion

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.parotrash.ui.pantallas.PantallaCodigoSatisfactorio
import com.example.parotrash.ui.pantallas.PantallaInicioSesion
import com.example.parotrash.ui.pantallas.PantallaRecuperarContraseña
import com.example.parotrash.ui.pantallas.PantallaRegistrarse
import com.example.parotrash.ui.viewmodel.InicioSesionViewModel

@Composable
fun NavegacionApp(
    viewModel: InicioSesionViewModel,
    navController: NavHostController = rememberNavController()
) {
    NavHost(
        navController = navController,
        startDestination = Pantallas.InicioSesion.ruta
    ) {
        composable(Pantallas.InicioSesion.ruta) {
            PantallaInicioSesion(
                viewModel = viewModel,
                irARegistro = {
                    navController.navigate(Pantallas.Registrarse.ruta)
                },
                irARecuperar = {
                    navController.navigate(Pantallas.RecuperarContrasena.ruta)
                }
            )
        }

        composable(Pantallas.Registrarse.ruta) {
            PantallaRegistrarse(
                viewModel = viewModel,
                irAInicio = {
                    navController.popBackStack()
                }
            )
        }

        composable(Pantallas.RecuperarContrasena.ruta) {
            PantallaRecuperarContraseña(
                viewModel = viewModel,
                irAInicio = {
                    navController.popBackStack()
                },
                irACodigo = {
                    navController.navigate(Pantallas.CodigoSatisfactorio.ruta)
                }
            )
        }

        composable(Pantallas.CodigoSatisfactorio.ruta) {
            PantallaCodigoSatisfactorio(
                viewModel = viewModel,
                irAInicio = {
                    navController.navigate(Pantallas.InicioSesion.ruta) {
                        popUpTo(Pantallas.InicioSesion.ruta) {
                            inclusive = true
                        }
                        launchSingleTop = true
                    }
                }
            )
        }
    }
}