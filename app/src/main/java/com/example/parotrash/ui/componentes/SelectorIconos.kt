package com.example.parotrash.ui.componentes

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
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

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        AnimatedVisibility(
            visible = expandido,
            enter = expandVertically(),
            exit = shrinkVertically()
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                iconosLista.forEach { icono ->
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .width(64.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(Color.White.copy(alpha = 0.8f))
                            .clickable {
                                onIconoSeleccionado(icono.clave)
                                expandido = false
                            }
                            .padding(bottom = 4.dp)
                    ) {
                        Icon(
                            painter = painterResource(id = icono.drawableRes),
                            contentDescription = stringResource(id = icono.stringRes),
                            modifier = Modifier.size(56.dp),
                            tint = Color.Unspecified
                        )
                        Text(
                            text = stringResource(id = icono.stringRes),
                            fontSize = 10.sp,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(horizontal = 2.dp),
                            lineHeight = 10.sp
                        )
                    }
                }
            }
        }

        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .size(70.dp)
                .border(1.5.dp, Color.Red, CircleShape)
                .clip(CircleShape)
                .clickable { expandido = !expandido }
        ) {
            Icon(
                painter = painterResource(id = iconoPrincipal.drawableRes),
                contentDescription = stringResource(id = iconoPrincipal.stringRes),
                modifier = Modifier.size(50.dp),
                tint = Color.Unspecified
            )
        }
    }
}