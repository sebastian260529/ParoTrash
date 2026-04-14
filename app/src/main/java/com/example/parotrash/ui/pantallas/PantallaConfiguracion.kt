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
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.parotrash.ui.componentes.BotonNavegacion
import com.example.parotrash.ui.componentes.Cabecera
import com.example.parotrash.ui.componentes.Logo
import com.example.parotrash.ui.theme.ParoTrashTheme

@Composable
fun PantallaConfiguracion(
    irAMiCuenta: () -> Unit,
    irANotificaciones: () -> Unit,
    irAPrivacidad: () -> Unit,
    IrAHome: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Cabecera(
            texto = "Configuración",
            onCloseClick = IrAHome
        )

        Logo(modifier = Modifier.size(160.dp))

        Spacer(modifier = Modifier.height(60.dp))

        Column(
            verticalArrangement = Arrangement.spacedBy(60.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            BotonNavegacion(
                texto = "Mi cuenta",
                icon1 = Icons.Default.AccountCircle,
                onClick = irAMiCuenta
            )
            BotonNavegacion(
                texto = "Notificaciones",
                icon1 = Icons.Default.Notifications,
                onClick = irANotificaciones
            )
            BotonNavegacion(
                texto = "Privacidad",
                icon1 = Icons.Default.Lock,
                onClick = irAPrivacidad
            )
        }
    }
}




@Composable
@Preview (showBackground = true)
fun PreviewPantallaConfiguracion () {
    ParoTrashTheme() {
        PantallaConfiguracion(
            irAMiCuenta = {},
            irANotificaciones = {},
            irAPrivacidad = {},
            IrAHome = {}
        )
    }
}