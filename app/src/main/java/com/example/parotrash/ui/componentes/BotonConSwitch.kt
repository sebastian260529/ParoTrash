package com.example.parotrash.ui.componentes

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Brightness4
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.parotrash.ui.theme.ParoTrashTheme

@Composable
fun BotonConSwitch(
    icon1 : ImageVector,
    texto : String,
    estaActivado : Boolean,
    onCheckedChange : (Boolean) -> Unit,
    modifier : Modifier = Modifier,
    habilitado : Boolean = true,
    descripcion: String? = null

) {
    Column(
        modifier = modifier.fillMaxWidth()
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

            Switch(
                checked = estaActivado,
                onCheckedChange = onCheckedChange,
                enabled = habilitado,
                colors = SwitchDefaults.colors(
                    checkedThumbColor = MaterialTheme.colorScheme.primary,
                    checkedTrackColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.3f)
                )
            )
        }

        if (!descripcion.isNullOrEmpty()) {
            Text(
                text = descripcion,
                color = MaterialTheme.colorScheme.outline,
                fontSize = 12.sp,
                modifier = Modifier
                    .padding(start = 25.dp, top = 4.dp, end = 25.dp)
                    .alpha(if (habilitado) 1f else 0.5f)
            )
        }
    }

}


@Preview(showBackground = true)
@Composable
fun PreviewBotonConSwitch() {
    var estadoAlerta by remember { mutableStateOf(true) }
    var estadoOscuro by remember { mutableStateOf(false) }

    ParoTrashTheme() {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            BotonConSwitch(
                texto = "Recibir alertas",
                icon1 = Icons.Default.Info,
                estaActivado = estadoAlerta,
                onCheckedChange = { estadoAlerta = it }
            )

            BotonConSwitch(
                icon1 = Icons.Default.Brightness4,
                texto = "Modo oscuro",
                descripcion = "Ahorra batería y descansa la vista",
                estaActivado = estadoOscuro,
                onCheckedChange = { estadoOscuro = it }
            )
        }
    }
}
