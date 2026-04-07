package com.example.parotrash.ui.pantallas

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.parotrash.ui.viewmodel.InicioSesionViewModel
import com.example.parotrash.ui.componentes.BotonCargando
import com.example.parotrash.ui.componentes.Formulario
import com.example.parotrash.ui.componentes.Logo

@Composable
fun PantallaInicioSesion(viewModel: InicioSesionViewModel) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFFFFFF))
            .padding(horizontal = 16.dp, vertical = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        // Logo
        Logo(
            modifier = Modifier.size(160.dp)
        )

        Spacer(modifier = Modifier.height(50.dp))

        // Título "Inicio Sesión"
        Text(
            text = "Inicio Sesión",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF000000)
        )

        Spacer(modifier = Modifier.height(50.dp))

        // Campo Usuario/Email
        Formulario(
            icon1 = Icons.Default.Email,
            nombre = "Correo",
            abajo = null,
            icon2 = false,
            usuario = viewModel.email,
            onTextChange = { viewModel.updateEmail(it) }
        )

        Spacer(modifier = Modifier.height(32.dp))

        // Campo Contraseña
        Formulario(
            icon1 = Icons.Default.Lock,
            nombre = "Contraseña",
            abajo = viewModel.mensajeError,
            icon2 = true,
            usuario = viewModel.password,
            onTextChange = { viewModel.updatePassword(it) }
        )

        Spacer(modifier = Modifier.height(8.dp))

        // "¿Olvido su contraseña?"
        Text(
            text = "¿Olvidó su contraseña?",
            fontSize = 14.sp,
            color = Color(0xFF03A9F4),

        )

        Spacer(modifier = Modifier.height(24.dp))

        // Botón "Iniciar Sesión"
        BotonCargando(
            nombre = "Iniciar Sesión",
            isLoading = viewModel.isLoading,
            onClick = { viewModel.login() }
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Botón "Continuar Como Invitado"
        BotonCargando(
            nombre = "Continuar Como Invitado",
            isLoading = false,
            onClick = { viewModel.login() }
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Texto "¿No tienes cuenta?" y "Registrarte"
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "¿No tienes cuenta? ",
                fontSize = 14.sp,
                color = Color.Gray
            )
            Text(
                text = "Registrarte",
                fontSize = 14.sp,
                color = Color(0xFF03A9F4),
            )
        }
    }
}

// Preview
@Preview(showBackground = true)
@Composable
fun PreviewPantallaInicioSesion() {
    val fakeViewModel = InicioSesionViewModel()
    PantallaInicioSesion(viewModel = fakeViewModel)
}