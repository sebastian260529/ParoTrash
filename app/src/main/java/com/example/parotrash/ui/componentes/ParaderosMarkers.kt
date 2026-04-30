package com.example.parotrash.ui.componentes

import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import com.example.parotrash.modelos.ParaderoSITP
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.rememberMarkerState

@Composable
fun ParaderosMarkers(
    paraderos: List<ParaderoSITP>,
    icono: BitmapDescriptor,
    onParaderoClick: ((ParaderoSITP) -> Unit)? = null
) {
    paraderos.forEach { paradero ->
        val pos = LatLng(paradero.latitud, paradero.longitud)
        val direccion = paradero.direccion ?: "Dirección no disponible"

        key("paradero_${paradero.nombre}_${paradero.latitud}") {
            Marker(
                state = rememberMarkerState(position = pos),
                title = "Nom_est: ${paradero.nombre}",
                snippet = "Ubicación: $direccion",
                icon = icono,
                onClick = {
                    onParaderoClick?.invoke(paradero)
                    true
                }
            )
        }
    }
}