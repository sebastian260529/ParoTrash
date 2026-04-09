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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.parotrash.ui.componentes.BotonCargando
import com.example.parotrash.ui.componentes.Formulario
import com.example.parotrash.ui.componentes.Logo
import com.example.parotrash.ui.viewmodel.RecuperarViewModel

@Composable
fun PantallaRecuperarContraseña(
    viewModel: RecuperarViewModel,
    irAInicio: () -> Unit,
    irACodigo: () -> Unit
) {
    // Estados del ViewModel
    val correo = viewModel.correo
    val cargando = viewModel.cargando
    val correoEnviado = viewModel.correoEnviado

    // Navegar cuando el correo se envíe exitosamente
    LaunchedEffect(correoEnviado) {
        if (correoEnviado) {
            irACodigo()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFFFFFF))
            .padding(horizontal = 16.dp, vertical = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        // Logo
        Logo(
            modifier = Modifier.size(160.dp)
        )

        // Título
        Text(
            text = "Recuperar Contraseña",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF000000)
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Campo Correo
        Formulario(
            icon1 = Icons.Default.Email,
            nombre = "Correo",
            abajo = null,
            icon2 = false,
            usuario = correo,
            onTextChange = { viewModel.actualizarCorreo(it) }
        )



        Spacer(modifier = Modifier.height(8.dp))

        // Botón "Enviar correo"
        BotonCargando(
            nombre = "Enviar correo",
            isLoading = cargando,
            onClick = { viewModel.recuperarContraseña() }
        )

        // Texto "Volver a Inicio de Sesión"
        Text(
            text = "Volver a Inicio de Sesión",
            fontSize = 14.sp,
            color = Color(0xFF03A9F4),
            modifier = Modifier.clickable { irAInicio() }
        )

        Spacer(modifier = Modifier.height(16.dp))
    }
}