package com.example.parotrash.ui.pantallas

import android.Manifest
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.parotrash.R
import com.example.parotrash.data.SessionManager
import com.example.parotrash.modelos.Reporte
import com.example.parotrash.ui.componentes.BotonControlCircular
import com.example.parotrash.ui.componentes.DialogosHome
import com.example.parotrash.ui.componentes.MapMarkers
import com.example.parotrash.ui.componentes.SelectorIconos
import com.example.parotrash.ui.theme.ParoTrashTheme
import com.example.parotrash.ui.viewmodel.AlertasViewModel
import com.example.parotrash.ui.viewmodel.HomeViewModel
import com.example.parotrash.ui.viewmodel.UsuarioViewModel
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberPermissionState
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.maps.android.compose.*
import kotlinx.coroutines.launch
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.MyLocation
import com.google.accompanist.permissions.isGranted

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun PantallaHome(
    homeViewModel: HomeViewModel,
    usuarioViewModel: UsuarioViewModel,
    alertasViewModel: AlertasViewModel,
    irACerrarSesion: () -> Unit,
    irAConfiguracion: () -> Unit,
    sessionManager: SessionManager
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val ubicacion by homeViewModel.ubicacion.collectAsStateWithLifecycle()
    val isLoadingLocation by homeViewModel.isLoading.collectAsStateWithLifecycle()
    val reportes by homeViewModel.reportes.collectAsStateWithLifecycle()
    val scope = rememberCoroutineScope()

    var reporteSeleccionado by remember { mutableStateOf<String?>(null) }
    var reporteParaConfirmar by remember { mutableStateOf<Reporte?>(null) }
    val reportesConfirmados = remember { mutableStateListOf<String>() }

    val esModoOscuro = isSystemInDarkTheme()
    val cameraPositionState = rememberCameraPositionState()

    val iconosCacheados = rememberIconosReporte(context)

    val locationPermissionState = rememberPermissionState(Manifest.permission.ACCESS_FINE_LOCATION)

    // Limpiar alertas antiguas al iniciar
    LaunchedEffect(Unit) {
        alertasViewModel.limpiarAlertasAntiguas()
    }

    // Cerrar el diálogo cuando el reporte es exitoso
    LaunchedEffect(homeViewModel.reporteExito) {
        if (homeViewModel.reporteExito) {
            reporteSeleccionado = null
        }
    }

    // Solicitar permisos de ubicación al entrar por primera vez
    LaunchedEffect(Unit) {
        if (!locationPermissionState.status.isGranted) {
            locationPermissionState.launchPermissionRequest()
        }
    }

    // Obtener ubicación cuando el permiso esté concedido
    LaunchedEffect(locationPermissionState.status.isGranted) {
        if (locationPermissionState.status.isGranted) {
            homeViewModel.actualizarPermisos()
            homeViewModel.obtenerUbicacion()
        }
    }

    // Actualizar ubicación al volver de background
    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) {
                homeViewModel.actualizarPermisos()
                if (homeViewModel.tienePermiso()) {
                    homeViewModel.obtenerUbicacion()
                }
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose { lifecycleOwner.lifecycle.removeObserver(observer) }
    }

    // Mover cámara a ubicación actual
    LaunchedEffect(ubicacion) {
        ubicacion?.let {
            cameraPositionState.position = CameraPosition.fromLatLngZoom(
                LatLng(it.latitude, it.longitude), 17f
            )
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
            MapMarkers(
                reportes = reportes,
                iconos = iconosCacheados,
                zoomActual = cameraPositionState.position.zoom,
                reportesConfirmados = reportesConfirmados,
                onMarkerClick = { reporteParaConfirmar = it }
            )
        }

        if (isLoadingLocation) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
        }

        // Capa de UI
        Box(modifier = Modifier.fillMaxSize().padding(16.dp)) {
            Box(modifier = Modifier.align(Alignment.TopStart).widthIn(max = 400.dp)) {
                BotonControlCircular(
                    icon = Icons.Default.Menu,
                    onClick = irAConfiguracion,
                    conBorde = true
                )
            }

            Box(modifier = Modifier.align(Alignment.TopEnd).widthIn(max = 400.dp)) {
                BotonControlCircular(
                    icon = Icons.Default.MyLocation,
                    onClick = {
                        homeViewModel.obtenerUbicacion()
                        ubicacion?.let {
                            scope.launch {
                                cameraPositionState.animate(CameraUpdateFactory.newLatLngZoom(LatLng(it.latitude, it.longitude), 17f))
                            }
                        }
                    },
                    background = ParoTrashTheme.customColors.mapElementBackground
                )
            }

            Box(modifier = Modifier.align(Alignment.BottomEnd).widthIn(max = 400.dp)) {
                SelectorIconos(onIconoSeleccionado = { reporteSeleccionado = it })
            }
        }

        // Diálogos de Home
        DialogosHome(
            reporteSeleccionado = reporteSeleccionado,
            reporteParaConfirmar = reporteParaConfirmar,
            isLoading = homeViewModel.cargandoReporte,
            errorMessage = homeViewModel.errorReporte,
            onDismissReporte = { reporteSeleccionado = null },
            onConfirmarNuevoReporte = { tipo -> 
                homeViewModel.reportarRapido(tipo) {
                    usuarioViewModel.registrarNuevoReporte()
                }
            },
            onDismissConfirmacion = { reporteParaConfirmar = null },
            onConfirmarExistente = { id ->
                reportesConfirmados.add(id)
                alertasViewModel.confirmarAlerta(id) // Lógica de alertas
                usuarioViewModel.recibirConfirmacion() // Suma reputación
                reporteParaConfirmar = null
            },
            onDescartarExistente = { id ->
                alertasViewModel.descartarAlerta(id) // Lógica inteligente de eliminación
                reporteParaConfirmar = null
            }
        )
    }
}

@Composable
fun rememberIconosReporte(context: android.content.Context): Map<String, BitmapDescriptor>? {
    var iconos by remember { mutableStateOf<Map<String, BitmapDescriptor>?>(null) }
    LaunchedEffect(Unit) {
        try {
            val escala = 100
            fun crear(res: Int) = BitmapDescriptorFactory.fromBitmap(
                Bitmap.createScaledBitmap(BitmapFactory.decodeResource(context.resources, res), escala, escala, false)
            )
            iconos = mapOf(
                "Alerta" to crear(R.drawable.iconalertas),
                "Bus Varado" to crear(R.drawable.iconbus),
                "Accidente" to crear(R.drawable.iconchoque),
                "Manifestación" to crear(R.drawable.iconmanifestacion),
                "Default" to crear(R.drawable.iconalertas)
            )
        } catch (e: Exception) { Log.e("MapsError", "Iconos error: ${e.message}") }
    }
    return iconos
}
