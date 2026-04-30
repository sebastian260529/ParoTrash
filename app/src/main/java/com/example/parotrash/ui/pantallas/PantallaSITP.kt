package com.example.parotrash.ui.pantallas

import android.Manifest
import android.location.Location
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.MyLocation
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.OutlinedTextFieldDefaults
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
import com.example.parotrash.data.LocationManager
import com.example.parotrash.modelos.LugarBusqueda
import com.example.parotrash.modelos.ParaderoSITP
import com.example.parotrash.ui.componentes.BotonControlCircular
import com.example.parotrash.ui.componentes.DialogoConfirmarEstacion
import com.example.parotrash.ui.componentes.ParaderosMarkers
import com.example.parotrash.ui.theme.ParoTrashTheme
import com.example.parotrash.ui.viewmodel.SitpViewModel
import com.example.parotrash.ui.viewmodel.SitpViewModelFactory
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MapStyleOptions

import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.rememberCameraPositionState
import kotlinx.coroutines.launch

import com.google.android.gms.maps.CameraUpdateFactory

private val BOGOTA = LatLng(4.6091, -74.0817)

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun PantallaSITP(
    irAHome: () -> Unit,
    irARutasFavoritas: () -> Unit,
    onParaderoSeleccionado: ((String, Double, Double) -> Unit)? = null
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    val locationPermissionState = rememberPermissionState(Manifest.permission.ACCESS_FINE_LOCATION)
    val locationManager = remember { LocationManager(context) }
    var userLocation by remember { mutableStateOf<Location?>(null) }

    var textoBusqueda by remember { mutableStateOf("") }
    var paraderoSeleccionado by remember { mutableStateOf<ParaderoSITP?>(null) }
    var mostrarDialogoConfirmar by remember { mutableStateOf(false) }

    val viewModel: SitpViewModel = viewModel(
        factory = SitpViewModelFactory(context.applicationContext as android.app.Application)
    )
    val paraderos by viewModel.paraderos.collectAsStateWithLifecycle()
    val isLoading by viewModel.isLoading.collectAsStateWithLifecycle()
    val resultadosBusqueda by viewModel.resultadosBusqueda.collectAsStateWithLifecycle()

    val esModoOscuro = isSystemInDarkTheme()
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(BOGOTA, 11f)
    }

    val iconoParadero = rememberIconoParadero()

    LaunchedEffect(Unit) {
        if (!locationPermissionState.status.isGranted) {
            locationPermissionState.launchPermissionRequest()
        }
    }

    LaunchedEffect(locationPermissionState.status.isGranted) {
        if (locationPermissionState.status.isGranted) {
            userLocation = locationManager.getCurrentLocation()
            userLocation?.let {
                cameraPositionState.position = CameraPosition.fromLatLngZoom(
                    LatLng(it.latitude, it.longitude), 14f
                )
            }
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        GoogleMap(
            modifier = Modifier.fillMaxSize(),
            cameraPositionState = cameraPositionState,
            properties = MapProperties(
                isMyLocationEnabled = locationPermissionState.status.isGranted,
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
                        paraderoSeleccionado = paradero
                        mostrarDialogoConfirmar = true
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
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Volver"
                )
            }

            Row(modifier = Modifier.align(Alignment.TopEnd)) {
                BotonControlCircular(
                    icon = Icons.Default.MyLocation,
                    onClick = {
                        scope.launch {
                            userLocation = locationManager.getCurrentLocation()
                            userLocation?.let {
                                cameraPositionState.animate(
                                    CameraUpdateFactory.newLatLngZoom(LatLng(it.latitude, it.longitude), 16f)
                                )
                            }
                        }
                    },
                    background = ParoTrashTheme.customColors.mapElementBackground
                )
                Spacer(modifier = Modifier.width(8.dp))
                IconButton(onClick = irARutasFavoritas) {
                    Icon(
                        imageVector = Icons.Default.Star,
                        contentDescription = "Rutas Favoritas",
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            }

            Column(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .padding(bottom = 16.dp)
            ) {
                OutlinedTextField(
                    value = textoBusqueda,
                    onValueChange = { texto ->
                        textoBusqueda = texto
                        viewModel.buscarParaderos(texto)
                    },
                    placeholder = { Text("Buscar paradero...") },
                    leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = ParoTrashTheme.customColors.mapElementBackground,
                        unfocusedContainerColor = ParoTrashTheme.customColors.mapElementBackground
                    )
                )

                if (resultadosBusqueda.isNotEmpty() && textoBusqueda.isNotEmpty()) {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(150.dp)
                            .background(ParoTrashTheme.customColors.mapElementBackground)
                            .padding(horizontal = 8.dp)
                    ) {
                        items(resultadosBusqueda) { paradero ->
                            TextButton(
                                onClick = {
                                    paraderoSeleccionado = paradero
                                    mostrarDialogoConfirmar = true
                                },
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = Icons.Default.Place,
                                        contentDescription = null,
                                        tint = MaterialTheme.colorScheme.primary,
                                        modifier = Modifier.size(20.dp)
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Column {
                                        Text(
                                            text = paradero.nombre,
                                            style = MaterialTheme.typography.bodyMedium
                                        )
                                        paradero.direccion?.let { dir ->
                                            Text(
                                                text = dir,
                                                style = MaterialTheme.typography.bodySmall,
                                                color = MaterialTheme.colorScheme.outline
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        if (mostrarDialogoConfirmar && paraderoSeleccionado != null) {
            val lugar = LugarBusqueda(
                nombre = paraderoSeleccionado!!.nombre,
                latitud = paraderoSeleccionado!!.latitud,
                longitud = paraderoSeleccionado!!.longitud,
                tipo = "paradero"
            )
            DialogoConfirmarEstacion(
                lugar = lugar,
                onDismiss = { mostrarDialogoConfirmar = false },
                onConfirmar = {
                    onParaderoSeleccionado?.invoke(
                        paraderoSeleccionado!!.nombre,
                        paraderoSeleccionado!!.latitud,
                        paraderoSeleccionado!!.longitud
                    )
                }
            )
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