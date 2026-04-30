package com.example.parotrash.ui.pantallas

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.parotrash.R
import com.example.parotrash.ui.componentes.EstacionesMarkers
import com.example.parotrash.ui.viewmodel.EstacionesViewModel
import com.example.parotrash.ui.viewmodel.EstacionesViewModelFactory
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.rememberCameraPositionState

private val BOGOTA = LatLng(4.6091, -74.0817)

@Composable
fun PantallaTransmilenio(
    irAHome: () -> Unit,
    irARutasFavoritas: () -> Unit,
    onEstacionSeleccionada: ((String, Double, Double) -> Unit)? = null
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    val viewModel: EstacionesViewModel = viewModel(
        factory = EstacionesViewModelFactory(context.applicationContext as android.app.Application)
    )
    val estaciones by viewModel.estaciones.collectAsStateWithLifecycle()
    val isLoading by viewModel.isLoading.collectAsStateWithLifecycle()

    val esModoOscuro = isSystemInDarkTheme()
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(BOGOTA, 11f)
    }

    val iconosEstaciones = rememberIconosEstaciones(context)

    Box(modifier = Modifier.fillMaxSize()) {
        GoogleMap(
            modifier = Modifier.fillMaxSize(),
            cameraPositionState = cameraPositionState,
            properties = MapProperties(
                isMyLocationEnabled = false,
                mapStyleOptions = if (esModoOscuro) {
                    try { MapStyleOptions.loadRawResourceStyle(context, R.raw.mapa_oscuro) } catch (e: Exception) { null }
                } else null
            ),
            uiSettings = MapUiSettings(zoomControlsEnabled = false, myLocationButtonEnabled = false)
        ) {
            if (iconosEstaciones != null) {
                EstacionesMarkers(
                    estaciones = estaciones,
                    iconos = iconosEstaciones,
                    onEstacionClick = { estacion ->
                        onEstacionSeleccionada?.invoke(
                            estacion.nomEst,
                            estacion.latitud,
                            estacion.longitud
                        )
                    }
                )
            }
        }

        if (isLoading) {
            CircularProgressIndicator(
                modifier = Modifier
                    .align(Alignment.Center)
                    .size(48.dp)
            )
        }

        Box(modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
        ) {
            IconButton(
                onClick = irAHome,
                modifier = Modifier.align(Alignment.TopStart)
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Volver"
                )
            }

            IconButton(
                onClick = irARutasFavoritas,
                modifier = Modifier.align(Alignment.TopEnd)
            ) {
                Icon(
                    imageVector = Icons.Default.Star,
                    contentDescription = "Rutas Favoritas",
                    tint = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}

@Composable
private fun rememberIconosEstaciones(context: android.content.Context): Map<Int, BitmapDescriptor>? {
    var iconos by remember { mutableStateOf<Map<Int, BitmapDescriptor>?>(null) }
    LaunchedEffect(Unit) {
        try {
            fun crear(color: Float) = BitmapDescriptorFactory.defaultMarker(color)
            iconos = mapOf(
                1 to crear(BitmapDescriptorFactory.HUE_RED),
                2 to crear(BitmapDescriptorFactory.HUE_YELLOW),
                3 to crear(BitmapDescriptorFactory.HUE_GREEN),
                4 to crear(BitmapDescriptorFactory.HUE_AZURE)
            )
        } catch (e: Exception) { }
    }
    return iconos
}