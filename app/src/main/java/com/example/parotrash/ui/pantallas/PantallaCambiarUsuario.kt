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
fun PantallaCambiarUsuario(
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
            texto = "Cambiar usuario",
            onBackClick = irAConfiguracion,
            onCloseClick = irAHome
        )

        Spacer(modifier = Modifier.height(30.dp))

        Logo(
            modifier = Modifier.size(120.dp)
        )

        Text(
            text = "Recuerda que al cambiar tu usuario lo puedes volver a cambiar luego de 7 dias.",
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
                icon1 = Icons.Default.AccountCircle,
                texto = "Usuario actual",
                onClick = {/* No hace nada*/}

            )

            Formulario(
                icon1 = Icons.Default.AccountCircle,
                nombre = "Usuario nuevo",
                abajo = "Mínimo: 3 caracteres-Máximo: 30 caracteres",
                icon2 = false,
                usuario = "Usuario Nuevo",
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
fun PantallaCambiarUsuarioPreview() {
    ParoTrashTheme() {
        PantallaCambiarUsuario(
            irAHome = {},
            irAConfiguracion = {},
            irARegistroExitoso = {},
            irAConfirmarDescartarCambios = {}
        )
    }
}