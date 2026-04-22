package com.example.parotrash.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

@Immutable
data class CustomColors(
    val mapElementBackground: Color = Color.Unspecified
)

val LocalCustomColors = staticCompositionLocalOf { CustomColors() }

private val DarkColorScheme = darkColorScheme(
    primary = Rojo,
    onPrimary = TextoBotonesBlack,
    secondary = AzulNavegacion,
    onSecondary = Color.White,
    background = FondoBlack,
    onBackground = TextoPrincipalBlack,
    surface = SuperficieBlack,
    onSurface = TextoPrincipalBlack,
    outline = GrisDetalle
)

private val LightColorScheme = lightColorScheme(
    primary = Rojo,
    onPrimary = TextoBotonesLight,
    secondary = AzulNavegacion,
    onSecondary = Color.White,
    background = FondoLight,
    onBackground = TextoPrincipalLight,
    surface = SuperficieLight,
    onSurface = TextoPrincipalLight,
    outline = GrisDetalle
)

@Composable
fun ParoTrashTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    val customColors = CustomColors(
        mapElementBackground = if (darkTheme) MapElementBackgroundDark else MapElementBackgroundLight
    )

    CompositionLocalProvider(LocalCustomColors provides customColors) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = Typography,
            content = content
        )
    }
}

// Objeto para acceder fácilmente a los colores personalizados
object ParoTrashTheme {
    val customColors: CustomColors
        @Composable
        get() = LocalCustomColors.current
}