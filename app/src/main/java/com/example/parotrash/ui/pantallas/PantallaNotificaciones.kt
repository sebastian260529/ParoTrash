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
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.EmojiPeople
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.parotrash.R
import com.example.parotrash.ui.componentes.BotonConSwitch

import com.example.parotrash.ui.componentes.Cabecera
import com.example.parotrash.ui.theme.ParoTrashTheme

@Composable
fun PantallaNotificaciones(
    irAHome: () -> Unit,
    irAConfiguracion: () -> Unit
) {
    var recibirAlertas by remember { mutableStateOf(true) }
    var bloqueosViales by remember { mutableStateOf(true) }
    var manifestaciones by remember { mutableStateOf(true) }
    var rutasAlternativas by remember { mutableStateOf(true) }
    var comunidad by remember { mutableStateOf(true) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Cabecera(
            texto = stringResource(R.string.notificaciones_1),
            onBackClick = irAConfiguracion,
            onCloseClick = irAHome
        )

        Spacer(modifier = Modifier.height(30.dp))

        BotonConSwitch(
            icon1 = Icons.Default.Info,
            texto = stringResource(R.string.recibir_alertas),
            estaActivado = recibirAlertas,
            onCheckedChange = { recibirAlertas = it }
        )

        Spacer(modifier = Modifier.height(60.dp))

        Column(
            verticalArrangement = Arrangement.spacedBy(40.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = stringResource(R.string.notificaciones_2),
                fontSize = 22.sp,
                color = MaterialTheme.colorScheme.outline
            )

            BotonConSwitch(
                icon1 = Icons.Default.DirectionsCar,
                texto = stringResource(R.string.bloqueos_viales_1),
                estaActivado = bloqueosViales,
                onCheckedChange = { bloqueosViales = it },
                habilitado = recibirAlertas
            )

            BotonConSwitch(
                icon1 = Icons.Default.EmojiPeople,
                texto = stringResource(R.string.manifestaciones_1),
                estaActivado = manifestaciones,
                onCheckedChange = { manifestaciones = it },
                habilitado = recibirAlertas
            )

            BotonConSwitch(
                icon1 = Icons.Default.Warning,
                texto = stringResource(R.string.rutas_alternativas_1),
                estaActivado = rutasAlternativas,
                onCheckedChange = { rutasAlternativas = it },
                habilitado = recibirAlertas
            )

            BotonConSwitch(
                icon1 = Icons.Default.People,
                texto = stringResource(R.string.comunidad),
                estaActivado = comunidad,
                onCheckedChange = { comunidad = it },
                habilitado = recibirAlertas
            )

        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewPantallaNotificaciones() {
    ParoTrashTheme {
        PantallaNotificaciones(
            irAHome = {},
            irAConfiguracion = {}
        )
    }
}