package com.example.parotrash.ui.pantallas

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.parotrash.ui.componentes.BotonCargando
import com.example.parotrash.ui.componentes.Logo
import com.example.parotrash.ui.componentes.XError

@Composable
fun PantallaInicioSesionFallido(
    intentarDeNuevo: () -> Unit,
    irARegistro: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFFFFFF))
            .padding(horizontal = 16.dp, vertical = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Spacer(modifier = Modifier.height(40.dp))

        Logo(
            modifier = Modifier.size(170.dp)
        )

        Spacer(modifier = Modifier.height(40.dp))

        XError(
            modifier = Modifier.size(110.dp)
        )

        Spacer(modifier = Modifier.height(30.dp))

        Text(
            text = "Inicio de sesión fallido",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = "Ha ocurrido un error al iniciar sesión. Intente de nuevo",
            fontSize = 14.sp,
            color = Color.Black,
            textAlign = TextAlign.Center,
            lineHeight = 22.sp
        )

        Spacer(modifier = Modifier.height(20.dp))

        BotonCargando(
            nombre = "Intentar De Nuevo",
            isLoading = false,
            onClick = { intentarDeNuevo() }
        )

        Spacer(modifier = Modifier.height(60.dp))

        Text(
            text = "¿No tienes cuenta?",
            fontSize = 14.sp,
            color = Color.Black
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Registrate",
            fontSize = 14.sp,
            color = Color(0xFF03A9F4),
            modifier = Modifier.clickable { irARegistro() }
        )

        Spacer(modifier = Modifier.height(50.dp))
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewPantallaInicioSesionFallido() {
    PantallaInicioSesionFallido(
        intentarDeNuevo = {},
        irARegistro = {}
    )
}