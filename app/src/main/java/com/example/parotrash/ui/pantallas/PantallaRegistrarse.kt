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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.parotrash.R
import com.example.parotrash.ui.componentes.BotonCargando
import com.example.parotrash.ui.componentes.Formulario
import com.example.parotrash.ui.componentes.Logo
import com.example.parotrash.ui.viewmodel.RegistroViewModel

@Composable
fun PantallaRegistrarse(viewModel: RegistroViewModel, irAInicio: () -> Unit, irARegistroExitoso : () -> Unit) {
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


    LaunchedEffect(registroExitoso) {
        if (registroExitoso) {
            irARegistroExitoso()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 16.dp, vertical = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween

    ) {
        Logo(modifier = Modifier.size(150.dp))

        Text(
            text = stringResource(R.string.crear_cuenta),
            fontSize = 26.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground
        )

        Spacer(modifier = Modifier.height(16.dp))

        Formulario(
            icon1 = Icons.Default.AccountCircle,
            nombre = stringResource(R.string.nombre),
            abajo = errorNombre,
            icon2 = false,
            usuario = nombre,
            onTextChange = { viewModel.actualizarNombre(it) }
        )

        Formulario(
            icon1 = Icons.Default.Email,
            nombre = stringResource(R.string.correo_1),
            abajo = errorCorreo,
            icon2 = false,
            usuario = correo,
            onTextChange = { viewModel.actualizarCorreo(it) }
        )

        Formulario(
            icon1 = Icons.Default.Lock,
            nombre = stringResource(R.string.contrase_a_3),
            abajo = errorContraseña,
            icon2 = true,
            usuario = contraseña,
            onTextChange = { viewModel.actualizarContraseña(it) }
        )
        Formulario(
            icon1 = Icons.Default.Lock,
            nombre = stringResource(R.string.confirmar_contrase_a),
            abajo = errorConfirmar,
            icon2 = true,
            usuario = confirmarContraseña,
            onTextChange = { viewModel.actualizarConfirmarContraseña(it) }
        )

        if (errorGeneral != null) {
            Text(
                text = errorGeneral ?: "",
                color = MaterialTheme.colorScheme.primary,
                fontSize = 12.sp,
                modifier = Modifier.padding(8.dp)
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        BotonCargando(
            nombre = stringResource(R.string.registrarse),
            isLoading = cargando,
            onClick = { viewModel.registrarse() }
        )

        Spacer(modifier = Modifier.height(16.dp))

        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = stringResource(R.string.ya_tienes_cuenta),
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.outline
            )
            Text(
                text = stringResource(R.string.inicia_sesi_n),
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.secondary,
                modifier = Modifier.clickable { irAInicio() }
            )
        }

        Spacer(modifier = Modifier.height(24.dp))
    }
}