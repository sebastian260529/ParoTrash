package com.example.parotrash.ui.componentes

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.parotrash.ui.theme.ParoTrashTheme

@Composable
fun BotonInformacion(
    icon1: ImageVector,
    texto: String,
    valorFirabase: String? = null,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .fillMaxWidth()
            .height(55.dp)
            .padding(horizontal = 5.dp)
            .clip(RoundedCornerShape(40.dp)) // Asegura que el efecto de clic sea redondeado
            .clickable { onClick() }
            .border(1.5.dp, MaterialTheme.colorScheme.primary, RoundedCornerShape(40.dp))
            .background(MaterialTheme.colorScheme.background)
            .padding(horizontal = 16.dp)
    ) {
        Icon(
            imageVector = icon1,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(24.dp)
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

        if (!valorFirabase.isNullOrEmpty()) {
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = valorFirabase,
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

@Composable
@Preview
fun PreviewBotonInformacion() {
    ParoTrashTheme() {
        BotonInformacion(
            icon1 = Icons.Default.Lock,
            texto = "Contraseña",
            valorFirabase = "aaaaaaaaaaaassssssssssssssdddddddddddddd",
            onClick = {}
        )
    }

}