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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.parotrash.R
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
            texto = stringResource(R.string.privacidad_1),
            onBackClick = irAConfiguracion,
            onCloseClick = irAHome
        )

        Spacer(modifier = Modifier.height(30.dp))

        Text(
            text = stringResource(R.string.descripcion_5),
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
                texto = stringResource(R.string.ubicaci_n),
                estaActivado = ubicacion,
                onCheckedChange = { ubicacion = it },
                descripcion = stringResource(R.string.permitir_acceso_a_tu_ubicaci_n)
            )

            BotonConSwitch(
                icon1 = Icons.Default.Notifications,
                texto = stringResource(R.string.notificaciones_3),
                estaActivado = notificaciones,
                onCheckedChange = { notificaciones = it },
                descripcion = stringResource(R.string.descripcion_6)
            )

            BotonConSwitch(
                icon1 = Icons.Default.PushPin,
                texto = stringResource(R.string.almacenamiento),
                estaActivado = almacenamiento,
                onCheckedChange = { almacenamiento = it },
                descripcion = stringResource(R.string.descripcion_7)
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