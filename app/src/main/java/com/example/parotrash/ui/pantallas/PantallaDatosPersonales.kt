package com.example.parotrash.ui.pantallas

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.PhoneAndroid
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.parotrash.ui.componentes.Cabecera
import com.example.parotrash.ui.componentes.SeccionDato
import com.example.parotrash.ui.theme.ParoTrashTheme

@Composable
fun PantallaDatosPersonales(
    irAHome: () -> Unit,
    irAConfiguracion: () -> Unit,
    irACambioDireccion: () -> Unit,
    irACambioTelefono: () -> Unit,
    irACambioCorreo: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Cabecera(
            texto = "Datos personales",
            onBackClick = irAConfiguracion,
            onCloseClick = irAHome
        )

        Spacer(modifier = Modifier.height(60.dp))

        Column(
            verticalArrangement = Arrangement.spacedBy(40.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            SeccionDato(
                icon1 = Icons.Default.LocationOn,
                titulo = "Dirección",
                valor = "Cll. Bicentenario Segunda #12a-43c",
                onClick = irACambioDireccion
            )

            SeccionDato(
                icon1 = Icons.Default.PhoneAndroid,
                titulo = "Telefono",
                valor = "+57 3549876547",
                onClick = irACambioTelefono
            )

            SeccionDato(
                icon1 = Icons.Default.Email,
                titulo = "Correo electrónico",
                valor = "carlos.gomez@email.com",
                onClick = irACambioCorreo
            )
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun PantallaDatosPersonalesPreview() {
    MaterialTheme {
        ParoTrashTheme() {
            PantallaDatosPersonales(
                irAHome = {},
                irAConfiguracion = {},
                irACambioDireccion = {},
                irACambioTelefono = {},
                irACambioCorreo = {}
            )
        }
    }
}