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
import com.example.parotrash.ui.viewmodel.RecuperarViewModel

@Composable
fun PantallaRecuperarContraseña(
    viewModel: RecuperarViewModel,
    irARegistro: () -> Unit,
    irACodigo: () -> Unit
) {
    // Estados del ViewModel
    val correo = viewModel.correo
    val cargando = viewModel.cargando
    val correoEnviado = viewModel.correoEnviado
    val mensaje = viewModel.mensaje

    // Navegar cuando el correo se envíe exitosamente
    LaunchedEffect(correoEnviado) {
        if (correoEnviado) {
            irACodigo()
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
        // Logo
        Logo(
            modifier = Modifier.size(160.dp)
        )

        // Título
        Text(
            text = stringResource(R.string.recuperar_contrase_a_1),
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Campo Correo
        Formulario(
            icon1 = Icons.Default.Email,
            nombre = stringResource(R.string.correo_2),
            abajo =mensaje,
            icon2 = false,
            usuario = correo,
            onTextChange = { viewModel.actualizarCorreo(it) }
        )



        Spacer(modifier = Modifier.height(8.dp))

        BotonCargando(
            nombre = stringResource(R.string.enviar_correo),
            isLoading = cargando,
            onClick = { viewModel.recuperarContraseña() }
        )
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = stringResource(R.string.no_tienes_cuenta_1),
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.outline
            )
            Text(
                text = stringResource(R.string.registrate),
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.secondary,
                modifier = Modifier.clickable { irARegistro() }
            )
        }

        Spacer(modifier = Modifier.height(16.dp))
    }
}