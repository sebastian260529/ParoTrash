package com.example.parotrash.ui.componentes

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.parotrash.ui.theme.ParoTrashTheme

@Composable
fun Labelnformacion(
    icon1 : ImageVector,
    texto : String,
    valorFirabase : String,
    modifier : Modifier = Modifier,
    ) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .fillMaxWidth()
            .height(50.dp)
            .padding(horizontal = 5.dp)
            .border(1.dp, MaterialTheme.colorScheme.primary, RoundedCornerShape(40.dp))
            .background(MaterialTheme.colorScheme.background, RoundedCornerShape(40.dp))
            .padding(horizontal = 16.dp)
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
            modifier = Modifier.weight(1f)
        )

        Text(
            text = valorFirabase,
            color = MaterialTheme.colorScheme.outline,
            fontSize = 14.sp,
            textAlign = TextAlign.End
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewLabelnformacion() {
    ParoTrashTheme() {
        Labelnformacion(
            icon1 = Icons.Default.Email,
            texto = "Correo electronico",
            valorFirabase = "se@gmail.com"
        )
    }
}