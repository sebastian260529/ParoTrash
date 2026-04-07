package com.example.parotrash.ui.pantallas

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
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
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Logo(
            modifier = Modifier.size(150.dp)
        )
        // Campo email (sin mensaje de error aquí)
        Formulario(
            icon1 = Icons.Default.Email,
            nombre = "Correo",
            abajo = null,  // ← Email no muestra error
            icon2 = false,
            usuario = viewModel.email,
            onTextChange = { viewModel.updateEmail(it) }
        )

        // Campo contraseña (muestra el error aquí)
        Formulario(
            icon1 = Icons.Default.Lock,
            nombre = "Contraseña",
            abajo = viewModel.mensajeError,  // ← El error se muestra aquí
            icon2 = true,
            usuario = viewModel.password,
            onTextChange = { viewModel.updatePassword(it) }
        )

        // Botón de login
        BotonCargando(
            nombre = "Iniciar sesión",
            isLoading = viewModel.isLoading,
            onClick = { viewModel.login() }
        )
    }
}
@Preview(showBackground = true)
@Composable
fun PreviewPantallaInicioSesion() {
    val fakeViewModel = InicioSesionViewModel()

    PantallaInicioSesion(viewModel = fakeViewModel)
}