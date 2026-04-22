package com.example.parotrash.ui.pantallas

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.MyLocation
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.parotrash.R
import com.example.parotrash.data.SessionManager
import com.example.parotrash.ui.componentes.DialogoReporte
import com.example.parotrash.ui.componentes.SelectorIconos
import com.example.parotrash.ui.theme.ParoTrashTheme
import com.example.parotrash.ui.viewmodel.HomeViewModel
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.maps.android.compose.rememberMarkerState
import kotlinx.coroutines.launch

@Composable
fun PantallaHome(
    homeViewModel: HomeViewModel,
    irACerrarSesion: () -> Unit,
    irAConfiguracion: () -> Unit,
    sessionManager: SessionManager
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val ubicacion by homeViewModel.ubicacion.collectAsStateWithLifecycle()
    val isLoading by homeViewModel.isLoading.collectAsStateWithLifecycle()
    val permisoConcedido by homeViewModel.permisoConcedido.collectAsStateWithLifecycle()
    val reportes by homeViewModel.reportes.collectAsStateWithLifecycle()
    val scope = rememberCoroutineScope()

    var reporteSeleccionado by remember { mutableStateOf<String?>(null) }
    val esModoOscuro = isSystemInDarkTheme()

    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(LatLng(4.6097, -74.0817), 14f)
    }

    val zoomActual = cameraPositionState.position.zoom
    val umbralZoom = 13f

    // OPTIMIZACIÓN Y FIX: Estado para iconos cacheados
    var iconosCacheados by remember { mutableStateOf<Map<String, BitmapDescriptor>?>(null) }

    // Inicialización segura de iconos (Evita el crash de IBitmapDescriptorFactory)
    LaunchedEffect(Unit) {
        try {
            val escala = 100
            fun crearIcono(resId: Int): BitmapDescriptor {
                val bitmap = BitmapFactory.decodeResource(context.resources, resId)
                val rescalado = Bitmap.createScaledBitmap(bitmap, escala, escala, false)
                return BitmapDescriptorFactory.fromBitmap(rescalado)
            }
            iconosCacheados = mapOf(
                "Alerta" to crearIcono(R.drawable.iconalertas),
                "Bus Varado" to crearIcono(R.drawable.iconbus),
                "Accidente" to crearIcono(R.drawable.iconchoque),
                "Manifestación" to crearIcono(R.drawable.iconmanifestacion),
                "Default" to crearIcono(R.drawable.iconalertas)
            )
        } catch (e: Exception) {
            Log.e("MapsError", "Error al crear iconos: ${e.message}")
        }
    }

    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) {
                homeViewModel.actualizarPermisos()
                if (homeViewModel.tienePermiso() && homeViewModel.ubicacion.value == null) {
                    homeViewModel.obtenerUbicacion()
                }
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose { lifecycleOwner.lifecycle.removeObserver(observer) }
    }

    LaunchedEffect(permisoConcedido) {
        if (permisoConcedido) homeViewModel.obtenerUbicacion()
    }

    LaunchedEffect(ubicacion) {
        if (cameraPositionState.position.zoom < 10f) {
            ubicacion?.let {
                cameraPositionState.position = CameraPosition.fromLatLngZoom(
                    LatLng(it.latitude, it.longitude), 17f
                )
            }
        }
    }

    val mapProperties = remember(esModoOscuro, permisoConcedido) {
        MapProperties(
            isMyLocationEnabled = permisoConcedido,
            mapStyleOptions = if (esModoOscuro) {
                try { MapStyleOptions.loadRawResourceStyle(context, R.raw.mapa_oscuro) } catch (e: Exception) { null }
            } else null
        )
    }

    val mapUiSettings = remember { MapUiSettings(zoomControlsEnabled = false, myLocationButtonEnabled = false) }

    Box(modifier = Modifier.fillMaxSize()) {
        if (isLoading) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
        }

        GoogleMap(
            modifier = Modifier.fillMaxSize(),
            cameraPositionState = cameraPositionState,
            properties = mapProperties,
            uiSettings = mapUiSettings
        ) {
            // Solo dibujar si los iconos están cargados y el zoom es suficiente
            val iconos = iconosCacheados
            if (iconos != null && zoomActual >= umbralZoom) {
                reportes.forEach { reporte ->
                    val pos = LatLng(reporte.ubicacion[0], reporte.ubicacion[1])
                    val icono = iconos[reporte.tipo] ?: iconos["Default"]

                    key(reporte.id) {
                        Marker(
                            state = rememberMarkerState(position = pos),
                            title = reporte.tipo,
                            snippet = reporte.descripcion,
                            icon = icono
                        )
                    }
                }
            }
        }

        // Botón Menú
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .padding(top = 40.dp, start = 16.dp)
                .align(Alignment.TopStart)
                .size(60.dp)
                .border(2.dp, MaterialTheme.colorScheme.primary, CircleShape)
                .clip(CircleShape)
                .background(Color.Transparent)
                .clickable { irAConfiguracion() }
        ) {
            Icon(Icons.Default.Menu, "Menú", tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(35.dp))
        }

        // Botón Ubicarme
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .padding(top = 40.dp, end = 16.dp)
                .align(Alignment.TopEnd)
                .size(60.dp)
                .background(ParoTrashTheme.customColors.mapElementBackground, CircleShape)
                .clip(CircleShape)
                .clickable {
                    homeViewModel.obtenerUbicacion()
                    ubicacion?.let {
                        scope.launch {
                            cameraPositionState.animate(
                                CameraUpdateFactory.newLatLngZoom(LatLng(it.latitude, it.longitude), 17f)
                            )
                        }
                    }
                }
        ) {
            Icon(Icons.Default.MyLocation, "Ubicarme", tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(35.dp))
        }

        SelectorIconos(
            onIconoSeleccionado = { reporteSeleccionado = it },
            modifier = Modifier.padding(end = 16.dp, bottom = 32.dp).align(Alignment.BottomEnd)
        )

        // Diálogos de Reporte
        when (reporteSeleccionado) {
            "iconalertas" -> DialogoReporte("¿Desea reportar una alerta?", R.drawable.iconalertas, { reporteSeleccionado = null }) { homeViewModel.reportarRapido("Alerta"); reporteSeleccionado = null }
            "iconbus" -> DialogoReporte("¿Desea reportar un bus varado?", R.drawable.iconbus, { reporteSeleccionado = null }) { homeViewModel.reportarRapido("Bus Varado"); reporteSeleccionado = null }
            "iconchoque" -> DialogoReporte("¿Desea reportar un accidente?", R.drawable.iconchoque, { reporteSeleccionado = null }) { homeViewModel.reportarRapido("Accidente"); reporteSeleccionado = null }
            "iconmanifestacion" -> DialogoReporte("¿Desea reportar una manifestación?", R.drawable.iconmanifestacion, { reporteSeleccionado = null }) { homeViewModel.reportarRapido("Manifestación"); reporteSeleccionado = null }
        }
    }
}