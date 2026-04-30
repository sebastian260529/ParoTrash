package com.example.parotrash.ui.navegacion

import android.app.Application
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.parotrash.data.NotificationPreferences
import com.example.parotrash.data.PermissionPreferences
import com.example.parotrash.data.SessionManager
import com.example.parotrash.ui.pantallas.PantallaBienvenida
import com.example.parotrash.ui.pantallas.PantallaCambiarContrasena
import com.example.parotrash.ui.pantallas.PantallaCambiarCorreo
import com.example.parotrash.ui.pantallas.PantallaCambiarUsuario
import com.example.parotrash.ui.pantallas.PantallaCodigoSatisfactorio
import com.example.parotrash.ui.pantallas.PantallaConfiguracion
import com.example.parotrash.ui.pantallas.PantallaHome
import com.example.parotrash.ui.pantallas.PantallaInicioSesion
import com.example.parotrash.ui.pantallas.PantallaMiCuenta
import com.example.parotrash.ui.pantallas.PantallaNotificaciones
import com.example.parotrash.ui.pantallas.PantallaPermisos
import com.example.parotrash.ui.pantallas.PantallaRecuperarContraseña
import com.example.parotrash.ui.pantallas.PantallaRegistrarse
import com.example.parotrash.ui.pantallas.PantallaRegistroExitoso
import com.example.parotrash.ui.pantallas.PantallaTransmilenio
import com.example.parotrash.ui.pantallas.PantallaSITP
import com.example.parotrash.ui.pantallas.PantallaRutasFavoritas
import com.example.parotrash.modelos.LugarBusqueda
import com.example.parotrash.ui.viewmodel.AlertasViewModel
import com.example.parotrash.ui.viewmodel.HomeViewModel
import com.example.parotrash.ui.viewmodel.HomeViewModelFactory
import com.example.parotrash.ui.viewmodel.InicioSesionViewModel
import com.example.parotrash.ui.viewmodel.RecuperarViewModel
import com.example.parotrash.ui.viewmodel.RegistroViewModel
import com.example.parotrash.ui.viewmodel.UsuarioViewModel
import com.example.parotrash.ui.viewmodel.RutasFavoritasViewModel

@Composable
fun NavegacionApp(
    navController: NavHostController,
    startDestination: String,
    sessionManager: SessionManager,
    notificationPreferences: NotificationPreferences,
    permissionPreferences: PermissionPreferences

) {
    val loginViewModel: InicioSesionViewModel = viewModel()
    val registroViewModel: RegistroViewModel = viewModel()
    val recuperarViewModel: RecuperarViewModel = viewModel()
    val usuarioViewModel: UsuarioViewModel = viewModel()
    val alertasViewModel: AlertasViewModel = viewModel()
    val homeViewModelFactory = HomeViewModelFactory(LocalContext.current.applicationContext as Application)
    val homeViewModel: HomeViewModel = viewModel(factory = homeViewModelFactory)

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
                    navController.navigate(Pantallas.InicioSesion.ruta)
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
                homeViewModel = homeViewModel,
                usuarioViewModel = usuarioViewModel,
                alertasViewModel = alertasViewModel,
                irACerrarSesion = {
                    navController.navigate(Pantallas.InicioSesion.ruta) {
                        popUpTo(Pantallas.Home.ruta) {
                            inclusive = true
                        }
                    }
                },
                irAConfiguracion = {navController.navigate(Pantallas.Configuracion.ruta)},
                irARutasFavoritas = { navController.navigate(Pantallas.RutasFavoritas.ruta) },
                sessionManager = sessionManager
            )
        }
        composable(Pantallas.Configuracion.ruta) {
            PantallaConfiguracion(
                irAMiCuenta = {
                    navController.navigate(Pantallas.MiCuenta.ruta)
                },
                irANotificaciones = {
                    navController.navigate(Pantallas.Notificaciones.ruta)
                },
                irAPrivacidad = {
                    navController.navigate(Pantallas.Privacidad.ruta)
                    },
                IrAHome = {
                    navController.popBackStack()
                }
            )
        }
        composable(Pantallas.MiCuenta.ruta) {
            PantallaMiCuenta(
                sessionManager = sessionManager,
                loginViewModel = loginViewModel,
                irACambiarUsuario = {
                    navController.navigate(Pantallas.CambiarUsuario.ruta)
                },
                irACambiarCorreo = {
                    navController.navigate(Pantallas.CambiarCorreo.ruta)
                },
                irACambiarContrasena = {
                    navController.navigate(Pantallas.CambiarContrasena.ruta)
                },
                irAHome = {
                    navController.navigate(Pantallas.Home.ruta) {
                        popUpTo(Pantallas.MiCuenta.ruta) { inclusive = true }
                    }
                },
                irAConfiguracion = {
                    navController.popBackStack()
                },
                irAinicoSesion = {
                    navController.navigate(Pantallas.InicioSesion.ruta) {
                        popUpTo(0)
                    }
                }
            )
        }
        composable(Pantallas.CambiarUsuario.ruta) {
            PantallaCambiarUsuario(
                irAHome = {
                    navController.navigate(Pantallas.Home.ruta)
                },
                irAConfiguracion = {
                    navController.popBackStack()
                },
                irARegistroExitoso = {
                    navController.popBackStack()
                },
                irAConfirmarDescartarCambios = {
                    navController.popBackStack()
                }
            )
        }
        composable(Pantallas.CambiarCorreo.ruta) {
            PantallaCambiarCorreo(
                irAHome = {
                    navController.navigate(Pantallas.Home.ruta)
                },
                irAConfiguracion = {
                    navController.popBackStack()
                },
                irARegistroExitoso = {
                    navController.popBackStack()
                },
                irAConfirmarDescartarCambios = {
                    navController.popBackStack()
                }
            )
        }

        composable(Pantallas.CambiarContrasena.ruta) {
            PantallaCambiarContrasena(
                irAHome = {
                    navController.navigate(Pantallas.Home.ruta)
                },
                irAConfiguracion = {
                    navController.popBackStack()
                },
                irARegistroExitoso = {
                    navController.popBackStack()
                },
                irAConfirmarDescartarCambios = {
                    navController.popBackStack()
                },
                irARecuperarContrasena = {
                    navController.navigate(Pantallas.RecuperarContrasena.ruta)
                }
            )
        }
        composable(Pantallas.Notificaciones.ruta) {
            PantallaNotificaciones(
                irAHome = { navController.navigate(Pantallas.Home.ruta) },
                irAConfiguracion = { navController.popBackStack() },
                notificationPreferences = notificationPreferences
            )
        }
        composable(Pantallas.Privacidad.ruta) {
            PantallaPermisos(
                irAHome = {
                    navController.navigate(Pantallas.Home.ruta) {
                        popUpTo(Pantallas.Privacidad.ruta) { inclusive = true }
                    }
                },
                irAConfiguracion = {
                    navController.popBackStack()
                },
                permissionPreferences = permissionPreferences
            )
        }
        composable(Pantallas.Transmilenio.ruta) {
            val rutasViewModel: RutasFavoritasViewModel = viewModel()
            PantallaTransmilenio(
                irAHome = { navController.popBackStack() },
                irARutasFavoritas = { navController.navigate(Pantallas.RutasFavoritas.ruta) },
                onEstacionSeleccionada = { nombre, lat, lng ->
                    val lugar = LugarBusqueda(
                        nombre = nombre,
                        latitud = lat,
                        longitud = lng,
                        tipo = "estacion"
                    )
                    rutasViewModel.guardarSeleccionTemporal(lugar)
                    navController.navigate(Pantallas.RutasFavoritas.ruta) {
                        popUpTo(Pantallas.Home.ruta)
                    }
                }
            )
        }
        composable(Pantallas.SITP.ruta) {
            val rutasViewModel: RutasFavoritasViewModel = viewModel()
            PantallaSITP(
                irAHome = { navController.popBackStack() },
                irARutasFavoritas = { navController.navigate(Pantallas.RutasFavoritas.ruta) },
                onParaderoSeleccionado = { nombre, lat, lng ->
                    val lugar = LugarBusqueda(
                        nombre = nombre,
                        latitud = lat,
                        longitud = lng,
                        tipo = "paradero"
                    )
                    rutasViewModel.guardarSeleccionTemporal(lugar)
                    navController.navigate(Pantallas.RutasFavoritas.ruta) {
                        popUpTo(Pantallas.Home.ruta)
                    }
                }
            )
        }
        composable(Pantallas.RutasFavoritas.ruta) {
            PantallaRutasFavoritas(
                irAHome = { navController.popBackStack() },
                irATransmilenio = { navController.navigate(Pantallas.Transmilenio.ruta) },
                irASITP = { navController.navigate(Pantallas.SITP.ruta) }
            )
        }

    }

}