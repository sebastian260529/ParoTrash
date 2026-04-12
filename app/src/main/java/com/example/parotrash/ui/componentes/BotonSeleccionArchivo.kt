package com.example.parotrash.ui.componentes

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.parotrash.ui.theme.ParoTrashTheme

@Composable
fun BotonSeleccionArchivo(
    icon1 : ImageVector,
    texto : String,
    nombreArchivo : String,
    onClick : () -> Unit,
    modifier : Modifier = Modifier,
    habilitado : Boolean = true
) {
    Button(
        onClick = onClick,
        enabled = habilitado,
        shape = CircleShape,
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary),
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.background, //fondo
            contentColor = MaterialTheme.colorScheme.onBackground, //texto
            disabledContainerColor = MaterialTheme.colorScheme.background //fondo desactivado
        ),
        contentPadding = PaddingValues(horizontal = 16.dp),
        modifier = modifier
            .fillMaxWidth()
            .height(50.dp)
            .padding(horizontal = 5.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Icon(
                imageVector = icon1,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.width(12.dp))

            Text(
                text = texto,
                color = MaterialTheme.colorScheme.onBackground,
                fontSize = 14.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.weight(1f)
            )

            Spacer(modifier = Modifier.width(8.dp))

            Text(
                text = nombreArchivo,
                color = MaterialTheme.colorScheme.outline,
                fontSize = 14.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.weight(0.6f, fill = false),
                textAlign = TextAlign.End
            )
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF)
@Composable
fun PreviewBotonSeleccionArchivo() {
    ParoTrashTheme() {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            BotonSeleccionArchivo(
                icon1 = Icons.Default.Notifications,
                texto = "Sonido de Alerta",
                nombreArchivo = "aaaaaaaaaaaassssssssssssssdddddddddddddd.mp3",
                onClick = {}
            )
            BotonSeleccionArchivo(
                icon1 = Icons.Default.Notifications,
                texto = "Sonido de Alerta",
                nombreArchivo = "sound.mp3",
                onClick = {},
                habilitado = false
            )
        }
    }
}