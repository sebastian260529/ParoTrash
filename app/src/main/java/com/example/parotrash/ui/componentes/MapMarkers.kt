package com.example.parotrash.ui.componentes

import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import com.example.parotrash.modelos.Reporte
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.rememberMarkerState

@Composable
fun MapMarkers(
    reportes: List<Reporte>,
    iconos: Map<String, BitmapDescriptor>?,
    zoomActual: Float,
    reportesConfirmados: List<String>,
    onMarkerClick: (Reporte) -> Unit
) {
    if (iconos != null && zoomActual >= 13f) {
        reportes.forEach { reporte ->
            val pos = LatLng(reporte.ubicacion[0], reporte.ubicacion[1])
            val icono = iconos[reporte.tipo] ?: iconos["Default"]

            key(reporte.id) {
                Marker(
                    state = rememberMarkerState(position = pos),
                    title = reporte.tipo,
                    snippet = reporte.descripcion,
                    icon = icono,
                    onClick = {
                        if (reporte.id !in reportesConfirmados) {
                            onMarkerClick(reporte)
                            true
                        } else false
                    }
                )
            }
        }
    }
}
