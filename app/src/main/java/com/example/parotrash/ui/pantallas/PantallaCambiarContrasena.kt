package com.example.parotrash.ui.pantallas

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.parotrash.ui.componentes.BotonCargando
import com.example.parotrash.ui.componentes.Cabecera
import com.example.parotrash.ui.componentes.Formulario
import com.example.parotrash.ui.componentes.Logo
import com.example.parotrash.ui.theme.ParoTrashTheme

@Composable
fun PantallaCambiarContrasena(
    irAHome: () -> Unit,
    irAConfiguracion: () -> Unit,
    irARegistroExitoso: () -> Unit,
    irAConfirmarDescartarCambios: () -> Unit,
    irARecuerarContrasena: () -> Unit
) {
    val contrasenaActual = "Ab_1234567"
    val nuevaContrasena = "Ab_1234567"
    val error = "Error"
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Cabecera(
            texto = "Cambiar Contraseña",
            onBackClick = irAConfiguracion,
            onCloseClick = irAHome
        )

        Spacer(modifier = Modifier.height(30.dp))

        Logo(
            modifier = Modifier.size(120.dp)
        )

        Spacer(modifier = Modifier.height(24.dp))

        Column(
            verticalArrangement = Arrangement.spacedBy(20.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Formulario(
                icon1 = Icons.Default.Lock,
                nombre = "Contraseña Actual",
                abajo = error,
                icon2 = true,
                usuario = contrasenaActual,
                onTextChange = { }
            )

            Formulario(
                icon1 = Icons.Default.Lock,
                nombre = "Contraseña Nueva",
                abajo = error,
                icon2 = true,
                usuario = nuevaContrasena,
                onTextChange = { }
            )

            Formulario(
                icon1 = Icons.Default.Lock,
                nombre = "Confirmar Contraseña Nueva",
                abajo = error,
                icon2 = true,
                usuario = nuevaContrasena,
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

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "¿Olvidaste tu contraseña?",
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.outline
                )
                Text(
                    text = "Recuperar contraseña",
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.secondary,
                    modifier = Modifier.clickable { irARecuerarContrasena() }
                )
            }
        }
    }
}


@Preview(showBackground = true, showSystemUi = true)
@Composable
fun PantallaCambiarContrasenaPreview() {
    // Si usas un tema personalizado, envuélvelo en ParoTrashTheme { ... }
    ParoTrashTheme(){
        PantallaCambiarContrasena(
            irAHome = {},
            irAConfiguracion = {},
            irARegistroExitoso = {},
            irAConfirmarDescartarCambios = {},
            irARecuerarContrasena = {}
        )
    }
}