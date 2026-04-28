package com.example.parotrash.ui.componentes

import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import com.example.parotrash.modelos.EstacionTransmilenio
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.rememberMarkerState

@Composable
fun EstacionesMarkers(
    estaciones: List<EstacionTransmilenio>,
    iconos: Map<Int, BitmapDescriptor>
) {
    estaciones.forEach { estacion ->
        val pos = LatLng(estacion.latitud, estacion.longitud)
        val icono = iconos[estacion.tipoEsta] ?: iconos[4]
        val direccion = estacion.ubEst ?: "Dirección no disponible"

        key("estacion_${estacion.nomEst}_${estacion.latitud}") {
            Marker(
                state = rememberMarkerState(position = pos),
                title = "Nom_est: ${estacion.nomEst}",
                snippet = "Ubicación: $direccion",
                icon = icono,
                onClick = { false }
            )
        }
    }
}