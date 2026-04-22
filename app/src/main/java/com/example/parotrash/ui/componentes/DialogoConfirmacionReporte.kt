package com.example.parotrash.ui.componentes

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ThumbDown
import androidx.compose.material.icons.filled.ThumbUp
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
import com.example.parotrash.ui.theme.ParoTrashTheme

@Composable
fun DialogoConfirmacionReporte(
    tipo: String,
    iconoRes: Int,
    onDescartar: () -> Unit,
    onConfirmar: () -> Unit
) {
    Dialog(onDismissRequest = onDescartar) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            shape = RoundedCornerShape(40.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color.White
            ),
            border = BorderStroke(2.dp, Color.Red)
        ) {
            Column(
                modifier = Modifier
                    .padding(24.dp)
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                Text(
                    text = "¿Sigue el ${tipo.lowercase()}?",
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Medium,
                        fontSize = 22.sp,
                        lineHeight = 28.sp
                    ),
                    textAlign = TextAlign.Center,
                    color = Color.Black
                )

                Icon(
                    painter = painterResource(id = iconoRes),
                    contentDescription = null,
                    modifier = Modifier.size(150.dp),
                    tint = Color.Unspecified
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // Botón No (Thumbs Down)
                    OutlinedButton(
                        onClick = onDescartar,
                        modifier = Modifier
                            .weight(1f)
                            .height(56.dp),
                        shape = RoundedCornerShape(28.dp),
                        border = BorderStroke(1.dp, Color.Red),
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = Color.Black
                        )
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Text("No", fontSize = 16.sp)
                            Spacer(Modifier.width(8.dp))
                            Icon(Icons.Default.ThumbDown, contentDescription = null, tint = Color.Red)
                        }
                    }

                    // Botón Sigue (Thumbs Up)
                    Button(
                        onClick = onConfirmar,
                        modifier = Modifier
                            .weight(1f)
                            .height(56.dp),
                        shape = RoundedCornerShape(28.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.Red
                        )
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Text("Sigue", fontSize = 16.sp, color = Color.White)
                            Spacer(Modifier.width(8.dp))
                            Icon(Icons.Default.ThumbUp, contentDescription = null, tint = Color.White)
                        }
                    }
                }
            }
        }
    }
}
