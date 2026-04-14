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
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.AddLocation
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.PhoneAndroid
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.parotrash.ui.componentes.BotonConSwitch
import com.example.parotrash.ui.componentes.BotonNavegacion
import com.example.parotrash.ui.componentes.Cabecera
import com.example.parotrash.ui.theme.ParoTrashTheme

@Composable
fun PantallaPrivacidad(
    irAHome: () -> Unit,
    irAConfiguracion: () -> Unit,
    irADatosPersonales: () -> Unit,
    irAPermisos: () -> Unit,
) {
    var ubicacion by remember { mutableStateOf(true) }
    var publicidad by remember { mutableStateOf(true) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp)
    ) {
        Cabecera(
            texto = "Privacidad",
            onBackClick = irAConfiguracion,
            onCloseClick = irAHome
        )

        Spacer(modifier = Modifier.height(30.dp))

        BotonConSwitch(
            icon1 = Icons.Default.AddLocation,
            texto = "Recibir alertas",
            estaActivado = ubicacion,
            onCheckedChange = { ubicacion = it }
        )

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = "Permitir compartir tu ubicación en tiempo real y recibir alertas sobre bloqueos cercanos.",
            fontSize = 14.sp,
            color = MaterialTheme.colorScheme.outline
        )

        Spacer(modifier = Modifier.height(30.dp))

        Text(
            text = "Privacidad",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground
        )

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = "Controla tus preferencias de privacidad y el manejo de tus datos en la app.",
            fontSize = 14.sp,
            color = MaterialTheme.colorScheme.outline
        )

        Spacer(modifier = Modifier.height(24.dp))

        Column(
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            BotonNavegacion(
                texto = "Datos Personales",
                icon1 = Icons.Default.AccountCircle,
                onClick = irADatosPersonales
            )

            Text(
                text = "Consulta tu información personal y gestiona tus datos almacenados.",
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.outline
            )

            BotonNavegacion(
                texto = "permisos",
                icon1 = Icons.Default.Lock,
                onClick = irAPermisos
            )

            Text(
                text = "Administrar los permisos de la app, como acceso a la ubicación y notificaciones.",
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.outline
            )

            BotonConSwitch(
                icon1 = Icons.Default.PhoneAndroid,
                texto = "Publicidad Personalizada",
                estaActivado = publicidad,
                onCheckedChange = { publicidad = it }
            )

            Text(
                text = "Controlar si quieres recibir anuncios personalizados basados en tus intereses.",
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.outline
            )


        }
    }
}


@Composable
@Preview (showBackground = true)
fun PreviewPantallaPrivacidad() {
    ParoTrashTheme() {
        PantallaPrivacidad(
            irAHome = {},
            irAConfiguracion= {},
            irADatosPersonales= {},
            irAPermisos = {})
    }
}