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
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect

import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.maps.android.compose.CameraPositionState
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.rememberCameraPositionState
import com.example.parotrash.R
import com.example.parotrash.ui.componentes.ParaderosMarkers
import com.example.parotrash.ui.viewmodel.SitpViewModel
import com.example.parotrash.ui.viewmodel.SitpViewModelFactory

private val BOGOTA = LatLng(4.6091, -74.0817)

@Composable
fun PantallaSITP(
    irAHome: () -> Unit,
    irARutasFavoritas: () -> Unit,
    onParaderoSeleccionado: ((String, Double, Double) -> Unit)? = null
) {
    val context = LocalContext.current

    val viewModel: SitpViewModel = viewModel(
        factory = SitpViewModelFactory(context.applicationContext as android.app.Application)
    )
    val paraderos by viewModel.paraderos.collectAsStateWithLifecycle()
    val isLoading by viewModel.isLoading.collectAsStateWithLifecycle()

    val esModoOscuro = isSystemInDarkTheme()
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(BOGOTA, 11f)
    }

    val iconoParadero = rememberIconoParadero()

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
            if (iconoParadero != null) {
                ParaderosMarkers(
                    paraderos = paraderos,
                    icono = iconoParadero,
                    onParaderoClick = { paradero ->
                        onParaderoSeleccionado?.invoke(
                            paradero.nombre,
                            paradero.latitud,
                            paradero.longitud
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
private fun rememberIconoParadero(): BitmapDescriptor? {
    var icono by remember { mutableStateOf<BitmapDescriptor?>(null) }
    LaunchedEffect(Unit) {
        try {
            icono = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)
        } catch (e: Exception) { }
    }
    return icono
}