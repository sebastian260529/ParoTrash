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
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.PushPin
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.parotrash.ui.componentes.BotonConSwitch
import com.example.parotrash.ui.componentes.Cabecera
import com.example.parotrash.ui.theme.ParoTrashTheme

@Composable
fun PantallaPermisos(
    irAHome: () -> Unit,
    irAConfiguracion: () -> Unit,
) {
    var ubicacion by remember { mutableStateOf(true) }
    var notificaciones by remember { mutableStateOf(true) }
    var almacenamiento by remember { mutableStateOf(true) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Cabecera(
            texto = "Privacidad",
            onBackClick = irAConfiguracion,
            onCloseClick = irAHome
        )

        Spacer(modifier = Modifier.height(30.dp))

        Text(
            text = "Administra los permisos de la app para acceder a la ubicación, notificaciones y almacenamiento.",
            fontSize = 14.sp,
            color = MaterialTheme.colorScheme.outline
        )

        Spacer(modifier = Modifier.height(60.dp))

        Column(
            verticalArrangement = Arrangement.spacedBy(30.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            BotonConSwitch(
                icon1 = Icons.Default.PushPin,
                texto = "Ubicación",
                estaActivado = ubicacion,
                onCheckedChange = { ubicacion = it },
                descripcion = "Permitir acceso a tu ubicación."
            )

            BotonConSwitch(
                icon1 = Icons.Default.Notifications,
                texto = "Notificaciones",
                estaActivado = notificaciones,
                onCheckedChange = { notificaciones = it },
                descripcion = "Permitir recibir alertas y notificaciones en tu dispositivo."
            )

            BotonConSwitch(
                icon1 = Icons.Default.PushPin,
                texto = "Almacenamiento",
                estaActivado = almacenamiento,
                onCheckedChange = { almacenamiento = it },
                descripcion = "Permitir acceso al almacenamiento de tu dispositivo."
            )
        }
    }
}


@Preview(showBackground = true, showSystemUi = true)
@Composable
fun PantallaPermisosPreview() {
    MaterialTheme {
        ParoTrashTheme() {
            PantallaPermisos(
                irAHome = {},
                irAConfiguracion = {}
            )
        }
    }
}