package com.example.parotrash.ui.pantallas

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.parotrash.data.SessionManager
import com.example.parotrash.ui.componentes.BotonCargando
import com.example.parotrash.ui.componentes.Formulario
import com.example.parotrash.ui.componentes.Logo
import com.example.parotrash.ui.viewmodel.InicioSesionViewModel

@Composable
fun PantallaInicioSesion(
    viewModel: InicioSesionViewModel,
    irARegistro: () -> Unit,
    irARecuperar: () -> Unit,
    irAHome: () -> Unit,
    sessionManager: SessionManager
) {

    val correo = viewModel.correo
    val contrasena = viewModel.contraseña
    val cargando = viewModel.cargando
    val errorCorreo = viewModel.errorCorreo
    val errorContrasena = viewModel.errorContraseña
    val errorGeneral = viewModel.errorGeneral
    val loginExitoso = viewModel.loginExitoso

    LaunchedEffect(loginExitoso) {
        if (loginExitoso) {
            sessionManager.setLoggedIn(true)
            irAHome()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(horizontal = 16.dp, vertical = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Logo(modifier = Modifier.size(160.dp))

        Text(
            text = "Inicio Sesión",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onPrimary
        )

        Spacer(modifier = Modifier.height(16.dp))

        Formulario(
            icon1 = Icons.Default.Email,
            nombre = "Correo",
            abajo = errorCorreo,
            icon2 = false,
            usuario = correo,
            onTextChange = { viewModel.actualizarCorreo(it) }
        )

        Formulario(
            icon1 = Icons.Default.Lock,
            nombre = "Contraseña",
            abajo = errorContrasena,
            icon2 = true,
            usuario = contrasena,
            onTextChange = { viewModel.actualizarContraseña(it) }
        )

        if (errorGeneral != null) {
            Text(
                text = errorGeneral ?: "",
                color = MaterialTheme.colorScheme.primary,
                fontSize = 12.sp,
                modifier = Modifier.padding(8.dp)
            )
        }


        Text(
            text = "¿Olvidó su contraseña?",
            fontSize = 14.sp,
            color = MaterialTheme.colorScheme.secondary,
            modifier = Modifier.clickable {
                irARecuperar()
            }
        )

        BotonCargando(
            nombre = "Iniciar Sesión",
            isLoading = cargando,
            onClick = { viewModel.iniciarSesion() }
        )
        BotonCargando(
            nombre = "Continuar Como Invitado",
            isLoading = false,
            onClick = { viewModel.iniciarComoInvitado() }
        )

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "¿No tienes cuenta? ",
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.outline,
            )
            Text(
                text = "Registrarte",
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.secondary,
                modifier = Modifier.clickable {
                    irARegistro()
                }
            )
        }

        Spacer(modifier = Modifier.height(16.dp))
    }
}