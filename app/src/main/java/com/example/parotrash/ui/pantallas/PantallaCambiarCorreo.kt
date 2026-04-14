package com.example.parotrash.ui.pantallas

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.parotrash.ui.componentes.BotonCargando
import com.example.parotrash.ui.componentes.BotonInformacion
import com.example.parotrash.ui.componentes.Cabecera
import com.example.parotrash.ui.componentes.Formulario
import com.example.parotrash.ui.componentes.Logo
import com.example.parotrash.ui.theme.ParoTrashTheme

@Composable
fun PantallaCambiarCorreo(
    irAHome: () -> Unit,
    irAConfiguracion: () -> Unit,
    irARegistroExitoso: () -> Unit,
    irAConfirmarDescartarCambios: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Cabecera(
            texto = "Cambiar correo",
            onBackClick = irAConfiguracion,
            onCloseClick = irAHome
        )

        Spacer(modifier = Modifier.height(30.dp))

        Logo(
            modifier = Modifier.size(120.dp)
        )

        Text(
            text = "Asegurate de poner un correo electronico válido al que tengas acceso.",
            fontSize = 14.sp,
            color = MaterialTheme.colorScheme.outline,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(30.dp))

        Column(
            verticalArrangement = Arrangement.spacedBy(40.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            BotonInformacion(
                icon1 = Icons.Default.Email,
                texto = "Correo actual",
                onClick = {/* No hace nada*/}

            )

            Formulario(
                icon1 = Icons.Default.Email,
                nombre = "Correo nuevo",
                abajo = "",
                icon2 = false,
                usuario = "Correo Nuevo",
                onTextChange = { }
            )

            BotonCargando(
                nombre = "Confirmar Cambios",
                isLoading = false,
                onClick = irARegistroExitoso
            )

            BotonCargando(
                nombre = "Descartar Cambios",
                isLoading = false,
                onClick = irAConfirmarDescartarCambios
            )
        }
    }
}


@Preview(showBackground = true, showSystemUi = true)
@Composable
fun PreviewPantallaCambiarCorreo() {
    ParoTrashTheme() {
        PantallaCambiarCorreo(
            irAHome = {},
            irAConfiguracion = {},
            irARegistroExitoso = {},
            irAConfirmarDescartarCambios = {}
        )
    }
}