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
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.parotrash.ui.componentes.BotonCargando
import com.example.parotrash.ui.componentes.Formulario
import com.example.parotrash.ui.componentes.Logo
import com.example.parotrash.ui.viewmodel.InicioSesionViewModel

@Composable
fun PantallaRegistrarse(
    viewModel: InicioSesionViewModel,
    irAInicio: () -> Unit
) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(horizontal = 16.dp, vertical = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {

        Spacer(modifier = Modifier.height(24.dp))

        Logo(
            modifier = Modifier.size(140.dp)
        )

        Text(
            text = "Crear Cuenta",
            fontSize = 26.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black
        )

        Spacer(modifier = Modifier.height(8.dp))

        Formulario(
            icon1 = Icons.Default.AccountCircle,
            nombre = "Usuario",
            abajo = null,
            icon2 = false,
            usuario = viewModel.email,
            onTextChange = { viewModel.updateEmail(it) }
        )

        Formulario(
            icon1 = Icons.Default.Email,
            nombre = "Correo",
            abajo = null,
            icon2 = false,
            usuario = viewModel.email,
            onTextChange = { viewModel.updateEmail(it) }
        )

        Formulario(
            icon1 = Icons.Default.Lock,
            nombre = "Contraseña",
            abajo = viewModel.mensajeError,
            icon2 = true,
            usuario = viewModel.password,
            onTextChange = { viewModel.updatePassword(it) }
        )

        Formulario(
            icon1 = Icons.Default.Lock,
            nombre = "Confirmar Contraseña",
            abajo = viewModel.mensajeError,
            icon2 = true,
            usuario = viewModel.password,
            onTextChange = { viewModel.updatePassword(it) }
        )

        Spacer(modifier = Modifier.height(8.dp))

        BotonCargando(
            nombre = "Registrarse",
            isLoading = viewModel.isLoading,
            onClick = { viewModel.login() }
        )

        Spacer(modifier = Modifier.height(8.dp))

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "¿Ya tienes cuenta? ",
                fontSize = 14.sp,
                color = Color.Gray
            )
            Text(
                text = "Inicia Sesión",
                fontSize = 14.sp,
                color = Color(0xFF03A9F4),
                modifier = Modifier.clickable { irAInicio() }
            )
        }

        Spacer(modifier = Modifier.height(24.dp))
    }
}
// Preview
@Preview(showBackground = true)
@Composable
fun PreviewPantallaRegistrarse() {
    val fakeViewModel = InicioSesionViewModel()
    PantallaRegistrarse(
        viewModel = fakeViewModel,
        irAInicio = {}
    )
}