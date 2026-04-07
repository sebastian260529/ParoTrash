package com.example.parotrash.ui.pantallas

import androidx.compose.foundation.background
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

@Composable
fun PantallaBienvenida(
    irAInicioSesion: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFFFFFF))
            .padding(horizontal = 24.dp, vertical = 32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Spacer(modifier = Modifier.height(8.dp))

        Logo(
            modifier = Modifier.size(220.dp)
        )

        Spacer(modifier = Modifier.height(28.dp))

        Text(
            text = "Bienvenido a ParoTrash",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = "ParoTrash te ayuda a mantenerte informado sobre manifestaciones, bloqueos y afectaciones en la movilidad de la ciudad.",
            fontSize = 16.sp,
            color = Color.Black,
            textAlign = TextAlign.Center,
            lineHeight = 26.sp
        )

        Spacer(modifier = Modifier.height(10.dp))

        Text(
            text = "Consulta alertas en tiempo real, reporta puntos críticos y encuentra rutas alternativas para llegar a tu destino",
            fontSize = 16.sp,
            color = Color(0xFF333333),
            textAlign = TextAlign.Center,
            lineHeight = 26.sp
        )

        Spacer(modifier = Modifier.height(40.dp))

        BotonCargando(
            nombre = "Comenzar",
            isLoading = false,
            onClick = { irAInicioSesion() }
        )

        Spacer(modifier = Modifier.height(24.dp))
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewPantallaBienvenida() {
    PantallaBienvenida(
        irAInicioSesion = {}
    )
}