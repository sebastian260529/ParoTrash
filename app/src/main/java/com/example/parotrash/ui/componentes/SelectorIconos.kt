package com.example.parotrash.ui.componentes

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.parotrash.R

data class IconoItem(
    val drawableRes: Int,
    val stringRes: Int,
    val clave: String
)

@Composable
fun SelectorIconos(
    onIconoSeleccionado: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var expandido by remember { mutableStateOf(false) }
    var iconoSeleccionado by remember { mutableStateOf("cono") }

    val contexto = LocalContext.current

    val iconoPrincipal = IconoItem(
        drawableRes = R.drawable.cono,
        stringRes = R.string.cono,
        clave = "cono"
    )

    val iconosLista = listOf(
        IconoItem(
            drawableRes = R.drawable.iconalertas,
            stringRes = R.string.iconalertas,
            clave = "iconalertas"
        ),
        IconoItem(
            drawableRes = R.drawable.iconbus,
            stringRes = R.string.iconbus,
            clave = "iconbus"
        ),
        IconoItem(
            drawableRes = R.drawable.iconchoque,
            stringRes = R.string.iconchoque,
            clave = "iconchoque"
        ),
        IconoItem(
            drawableRes = R.drawable.iconmanifestacion,
            stringRes = R.string.iconmanifestacion,
            clave = "iconmanifestacion"
        )
    )

    val iconoActual = if (iconoSeleccionado == "cono") {
        iconoPrincipal
    } else {
        iconosLista.find { it.clave == iconoSeleccionado } ?: iconoPrincipal
    }

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.End,
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        AnimatedVisibility(
            visible = expandido,
            enter = expandVertically(),
            exit = shrinkVertically()
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(4.dp),
                horizontalAlignment = Alignment.End
            ) {
                iconosLista.forEach { icono ->
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(Color.White.copy(alpha = 0.8f))
                            .clickable {
                                iconoSeleccionado = icono.clave
                                onIconoSeleccionado(icono.clave)
                                expandido = false
                            }
                            .padding(8.dp)
                    ) {
                        Icon(
                            painter = painterResource(id = icono.drawableRes),
                            contentDescription = stringResource(id = icono.stringRes),
                            modifier = Modifier.size(32.dp),
                            tint = Color.Unspecified
                        )
                        Text(
                            text = stringResource(id = icono.stringRes),
                            fontSize = 10.sp,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        }

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .clip(RoundedCornerShape(8.dp))
                .background(Color.White.copy(alpha = 0.8f))
                .clickable {
                    if (expandido) {
                        expandido = false
                    } else {
                        expandido = true
                    }
                }
                .padding(8.dp)
        ) {
            Icon(
                painter = painterResource(id = iconoActual.drawableRes),
                contentDescription = stringResource(id = iconoActual.stringRes),
                modifier = Modifier.size(40.dp),
                tint = Color.Unspecified
            )
            Text(
                text = stringResource(id = iconoActual.stringRes),
                fontSize = 10.sp,
                textAlign = TextAlign.Center
            )
        }
    }
}