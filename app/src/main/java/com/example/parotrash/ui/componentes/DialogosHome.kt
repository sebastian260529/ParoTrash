package com.example.parotrash.ui.componentes

import androidx.compose.runtime.Composable
import com.example.parotrash.R
import com.example.parotrash.modelos.Reporte

@Composable
fun DialogosHome(
    reporteSeleccionado: String?,
    reporteParaConfirmar: Reporte?,
    isLoading: Boolean,
    errorMessage: String?,
    onDismissReporte: () -> Unit,
    onConfirmarNuevoReporte: (String) -> Unit,
    onDismissConfirmacion: () -> Unit,
    onConfirmarExistente: (String) -> Unit,
    onDescartarExistente: (String) -> Unit,
    isLoadingConfirmar: Boolean = false,
    isLoadingDescartar: Boolean = false,
    errorMessageConfirmar: String? = null
) {
    val config = mapOf(
        "iconalertas" to ("Alerta" to R.drawable.iconalertas),
        "iconbus" to ("Bus Varado" to R.drawable.iconbus),
        "iconchoque" to ("Accidente" to R.drawable.iconchoque),
        "iconmanifestacion" to ("Manifestación" to R.drawable.iconmanifestacion)
    )

    reporteSeleccionado?.let { key ->
        config[key]?.let { (titulo, icono) ->
            DialogoReporte(
                titulo = "¿Desea reportar un/a ${titulo.lowercase()}?",
                iconoRes = icono,
                isLoading = isLoading,
                errorMessage = errorMessage,
                onDescartar = onDismissReporte,
                onAceptar = { onConfirmarNuevoReporte(titulo) }
            )
        }
    }

    reporteParaConfirmar?.let { reporte ->
        val iconoRes = when(reporte.tipo) {
            "Alerta" -> R.drawable.iconalertas
            "Bus Varado" -> R.drawable.iconbus
            "Accidente" -> R.drawable.iconchoque
            "Manifestación" -> R.drawable.iconmanifestacion
            else -> R.drawable.iconalertas
        }
        DialogoConfirmacionReporte(
            tipo = reporte.tipo,
            iconoRes = iconoRes,
            isLoadingConfirmar = isLoadingConfirmar,
            isLoadingDescartar = isLoadingDescartar,
            errorMessage = errorMessageConfirmar,
            onDescartar = { onDescartarExistente(reporte.id) },
            onConfirmar = { onConfirmarExistente(reporte.id) }
        )
    }
}
