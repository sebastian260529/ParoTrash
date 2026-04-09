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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.parotrash.ui.componentes.BotonSimple
import com.example.parotrash.ui.componentes.Check

@Composable
fun PantallaCodigoSatisfactorio(
    irAInicioSesion: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFFFFFF))
            .padding(horizontal = 24.dp, vertical = 48.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        Check(
            modifier = Modifier.size(80.dp)
        )

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "Código enviado correctamente",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF000000),
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = "Revisa tu correo electrónico",
            fontSize = 16.sp,
            color = Color(0xFF666666),
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(32.dp))

        BotonSimple(
            texto = "Iniciar Sesión",
            onClick = irAInicioSesion
        )
    }
}