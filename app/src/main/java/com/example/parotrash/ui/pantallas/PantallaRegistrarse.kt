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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.parotrash.ui.viewmodel.RegistroViewModel
import com.example.parotrash.ui.componentes.BotonCargando
import com.example.parotrash.ui.componentes.Formulario
import com.example.parotrash.ui.componentes.Logo

@Composable
fun PantallaRegistrarse(viewModel: RegistroViewModel, irAInicio: () -> Unit, irARegistroExitoso : () -> Unit) {
    // Observar estados
    val nombre = viewModel.nombre
    val correo = viewModel.correo
    val contraseña = viewModel.contraseña
    val confirmarContraseña = viewModel.confirmarContraseña
    val cargando = viewModel.cargando
    val errorNombre = viewModel.errorNombre
    val errorCorreo = viewModel.errorCorreo
    val errorContraseña = viewModel.errorContraseña
    val errorConfirmar = viewModel.errorConfirmar
    val errorGeneral = viewModel.errorGeneral
    val registroExitoso = viewModel.registroExitoso

    // Navegar cuando sea exitoso
    LaunchedEffect(registroExitoso) {
        if (registroExitoso) {
            irARegistroExitoso()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 16.dp, vertical = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Logo(modifier = Modifier.size(140.dp))

        Text(
            text = "Crear Cuenta",
            fontSize = 26.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Campo Nombre
        Formulario(
            icon1 = Icons.Default.AccountCircle,
            nombre = "Nombre",
            abajo = errorNombre,
            icon2 = false,
            usuario = nombre,
            onTextChange = { viewModel.actualizarNombre(it) }
        )

        // Campo Correo
        Formulario(
            icon1 = Icons.Default.Email,
            nombre = "Correo",
            abajo = errorCorreo,
            icon2 = false,
            usuario = correo,
            onTextChange = { viewModel.actualizarCorreo(it) }
        )

        // Campo Contraseña
        Formulario(
            icon1 = Icons.Default.Lock,
            nombre = "Contraseña",
            abajo = errorContraseña,
            icon2 = true,
            usuario = contraseña,
            onTextChange = { viewModel.actualizarContraseña(it) }
        )

        // Campo Confirmar Contraseña
        Formulario(
            icon1 = Icons.Default.Lock,
            nombre = "Confirmar Contraseña",
            abajo = errorConfirmar,
            icon2 = true,
            usuario = confirmarContraseña,
            onTextChange = { viewModel.actualizarConfirmarContraseña(it) }
        )

        // Error general
        if (errorGeneral != null) {
            Text(
                text = errorGeneral ?: "",
                color = Color.Red,
                fontSize = 12.sp,
                modifier = Modifier.padding(8.dp)
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Botón Registrarse
        BotonCargando(
            nombre = "Registrarse",
            isLoading = cargando,
            onClick = { viewModel.registrarse() }
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Texto "¿Ya tienes cuenta?"
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
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