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
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.parotrash.ui.componentes.BotonCargando
import com.example.parotrash.ui.componentes.BotonInformacion
import com.example.parotrash.ui.componentes.Cabecera
import com.example.parotrash.ui.componentes.Logo
import com.example.parotrash.ui.theme.ParoTrashTheme

@Composable
fun PantallaMiCuenta(
    irACambiarUsuario: () -> Unit,
    irACambiarCorreo: () -> Unit,
    irACambiarContrasena: () -> Unit,
    irAHome: () -> Unit,
    irAConfiguracion: () -> Unit,
    irAinicoSesion: () -> Unit,
    irAConfirmarBorrarCuenta: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Cabecera(
            texto = "Mi Cuenta",
            onBackClick = irAConfiguracion,
            onCloseClick = irAHome
        )

        // TODO: Quitar logo, no se como poner el espcacio de la imagen
        Logo(modifier.size(160.dp))

        Spacer(modifier = Modifier.height(60.dp))

        Column(
            verticalArrangement = Arrangement.spacedBy(40.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            BotonInformacion(
                icon1 = Icons.Default.AccountCircle,
                texto = "Nombre de usiario",
                // TODO: Falta poner los valores obtenidos del firabase
                valorFirabase = "ayuiAS8763489",
                onClick = irACambiarUsuario
            )

            BotonInformacion(
                icon1 = Icons.Default.Email,
                texto = "Correo electronico",
                // TODO: Falta poner los valores obtenidos del firabase
                valorFirabase = "se@gmail . com",
                onClick = irACambiarCorreo
            )

            BotonInformacion(
                icon1 = Icons.Default.Lock,
                texto = "Contraseña",
                onClick = irACambiarContrasena
            )

            BotonCargando(
                icon1 = Icons.AutoMirrored.Filled.ExitToApp,
                isLoading = false,
                onClick = irAinicoSesion,
                nombre = "Cerrar Sesión"
            )

            BotonCargando(
                isLoading = false,
                onClick = irAConfirmarBorrarCuenta,
                nombre = "Eliminar Cuenta"
            )
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun PreviewPantallaMiCuenta() {
    ParoTrashTheme {
        Column {
            PantallaMiCuenta(
                irACambiarUsuario = {},
                irACambiarCorreo = {},
                irACambiarContrasena = {},
                irAHome = {},
                irAConfiguracion = {},
                irAinicoSesion = {},
                irAConfirmarBorrarCuenta = {}
            )
        }
    }
}