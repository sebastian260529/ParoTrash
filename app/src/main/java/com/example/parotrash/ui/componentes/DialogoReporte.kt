package com.example.parotrash.ui.componentes

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.parotrash.ui.theme.ParoTrashTheme

@Composable
fun DialogoReporte(
    titulo: String,
    iconoRes: Int,
    isLoading: Boolean = false,
    errorMessage: String? = null,
    onDescartar: () -> Unit,
    onAceptar: () -> Unit
) {
    Dialog(
        onDismissRequest = onDescartar,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            shape = RoundedCornerShape(40.dp),
            colors = CardDefaults.cardColors(
                containerColor = ParoTrashTheme.customColors.mapElementBackground
            ),
            border = androidx.compose.foundation.BorderStroke(2.dp, MaterialTheme.colorScheme.primary)
        ) {
            Column(
                modifier = Modifier
                    .padding(24.dp)
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                Text(
                    text = titulo,
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Medium,
                        fontSize = 20.sp,
                        lineHeight = 24.sp
                    ),
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.onSurface
                )

                // Icono
                Icon(
                    painter = painterResource(id = iconoRes),
                    contentDescription = null,
                    modifier = Modifier.size(150.dp),
                    tint = Color.Unspecified
                )

                // Mensaje de error abajo de la imagen
                if (errorMessage != null) {
                    Text(
                        text = errorMessage,
                        color = MaterialTheme.colorScheme.error,
                        fontSize = 12.sp,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    // Botón Descartar: Copiamos exactamente la altura, padding y forma de BotonCargando
                    OutlinedButton(
                        onClick = onDescartar,
                        enabled = !isLoading,
                        modifier = Modifier
                            .weight(1f)
                            .height(42.dp)
                            .padding(horizontal = 5.dp),
                        shape = RoundedCornerShape(100.dp),
                        border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.primary),
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = MaterialTheme.colorScheme.onSurface
                        )
                    ) {
                        Text("Descartar", fontSize = 14.sp)
                    }

                    // Botón Aceptar: BotonCargando ya tiene height 42 y padding horizontal 5 internamente
                    Box(modifier = Modifier.weight(1f)) {
                        BotonCargando(
                            nombre = "Aceptar",
                            isLoading = isLoading,
                            onClick = onAceptar
                        )
                    }
                }
            }
        }
    }
}
