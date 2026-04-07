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
fun PantallaRecuperarContraseña(
    viewModel: InicioSesionViewModel,
    irAInicio: () -> Unit,
    irACodigo: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFFFFFF))
            .padding(horizontal = 16.dp, vertical = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {


        Logo(
            modifier = Modifier.size(160.dp)
        )
        // Título "Inicio Sesión"
        Text(
            text = "Recuperar Contraseña",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF000000)
        )
        Spacer(modifier = Modifier.height(16.dp))

        // Campo Usuario/Email
        Formulario(
            icon1 = Icons.Default.Email,
            nombre = "Correo",
            abajo = null,
            icon2 = false,
            usuario = viewModel.email,
            onTextChange = { viewModel.updateEmail(it) }
        )



        BotonCargando(
            nombre = "Recuperar Contraseña",
            isLoading = viewModel.isLoading,
            onClick = {
                viewModel.recuperarPassword()
                irACodigo()
            }
        )
        

        // Texto "¿No tienes cuenta?" y "Registrarte"
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
        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewPantallaRecuperarContraseña() {
    val fakeViewModel = InicioSesionViewModel()
    PantallaRecuperarContraseña(
        viewModel = fakeViewModel,
        irAInicio = {},
        irACodigo = {}
    )
}