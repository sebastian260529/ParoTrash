package com.example.parotrash.ui.pantallas

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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.parotrash.R
import com.example.parotrash.data.SessionManager
import com.example.parotrash.ui.componentes.DialogoReporte
import com.example.parotrash.ui.componentes.SelectorIconos
import com.example.parotrash.ui.theme.ParoTrashTheme
import com.example.parotrash.ui.viewmodel.HomeViewModel
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.rememberCameraPositionState
import kotlinx.coroutines.launch

@Composable
fun PantallaHome(
    homeViewModel: HomeViewModel,
    irACerrarSesion: () -> Unit,
    irAConfiguracion: () -> Unit,
    sessionManager: SessionManager
) {
    val context = LocalContext.current
    val ubicacion by homeViewModel.ubicacion.collectAsState()
    val isLoading by homeViewModel.isLoading.collectAsState()
    val scope = rememberCoroutineScope()
    
    // Estado para controlar qué diálogo mostrar
    var reporteSeleccionado by remember { mutableStateOf<String?>(null) }
    
    // Detectamos si el sistema está en modo oscuro
    val esModoOscuro = isSystemInDarkTheme()

    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(
            LatLng(4.6097, -74.0817),
            14f
        )
    }

    LaunchedEffect(Unit) {
        if (homeViewModel.tienePermiso()) {
            homeViewModel.obtenerUbicacion()
        }
    }

    LaunchedEffect(ubicacion) {
        ubicacion?.let { location ->
            cameraPositionState.position = CameraPosition.fromLatLngZoom(
                LatLng(location.latitude, location.longitude),
                17f
            )
        }
    }

    // Configuramos las propiedades del mapa con el estilo automático
    val mapProperties = remember(esModoOscuro) {
        MapProperties(
            isMyLocationEnabled = homeViewModel.tienePermiso(),
            mapStyleOptions = if (esModoOscuro) {
                try {
                    MapStyleOptions.loadRawResourceStyle(context, R.raw.mapa_oscuro)
                } catch (e: Exception) {
                    null
                }
            } else {
                null
            }
        )
    }

    val mapUiSettings = remember {
        MapUiSettings(
            zoomControlsEnabled = false,
            myLocationButtonEnabled = false
        )
    }

    Box(modifier = Modifier.fillMaxSize()) {
        if (isLoading) {
            CircularProgressIndicator(
                modifier = Modifier.align(Alignment.Center)
            )
        }

        GoogleMap(
            modifier = Modifier.fillMaxSize(),
            cameraPositionState = cameraPositionState,
            properties = mapProperties,
            uiSettings = mapUiSettings
        )

        // Botón de Menú (Superior Izquierda)
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
            Icon(
                imageVector = Icons.Default.Menu,
                contentDescription = "Menú",
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(35.dp)
            )
        }

        // Botón de Ubicarme (Superior Derecha)
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .padding(top = 40.dp, end = 16.dp)
                .align(Alignment.TopEnd)
                .size(60.dp)
                .background(ParoTrashTheme.customColors.mapElementBackground, CircleShape)
                .border(1.dp, Color.Gray.copy(alpha = 0.5f), CircleShape)
                .clip(CircleShape)
                .clickable {
                    scope.launch {
                        ubicacion?.let {
                            cameraPositionState.position = CameraPosition.fromLatLngZoom(
                                LatLng(it.latitude, it.longitude),
                                17f
                            )
                        }
                    }
                }
        ) {
            Icon(
                imageVector = Icons.Default.MyLocation,
                contentDescription = "Ubicarme",
                modifier = Modifier.size(35.dp),
                tint = MaterialTheme.colorScheme.primary
            )
        }

        // Selector de Iconos (Cono) - A la derecha
        SelectorIconos(
            onIconoSeleccionado = { icono ->
                reporteSeleccionado = icono
            },
            modifier = Modifier
                .padding(end = 16.dp, bottom = 32.dp)
                .align(Alignment.BottomEnd)
        )

        // Lógica de Diálogos de Reporte
        when (reporteSeleccionado) {
            "iconalertas" -> DialogoReporte(
                titulo = "¿Desea reportar una alerta?",
                iconoRes = R.drawable.iconalertas,
                onDescartar = { reporteSeleccionado = null },
                onAceptar = { 
                    homeViewModel.reportarRapido("Alerta")
                    reporteSeleccionado = null 
                }
            )
            "iconbus" -> DialogoReporte(
                titulo = "¿Desea reportar un bus varado?",
                iconoRes = R.drawable.iconbus,
                onDescartar = { reporteSeleccionado = null },
                onAceptar = { 
                    homeViewModel.reportarRapido("Bus Varado")
                    reporteSeleccionado = null 
                }
            )
            "iconchoque" -> DialogoReporte(
                titulo = "¿Desea reportar un accidente?",
                iconoRes = R.drawable.iconchoque,
                onDescartar = { reporteSeleccionado = null },
                onAceptar = { 
                    homeViewModel.reportarRapido("Accidente")
                    reporteSeleccionado = null 
                }
            )
            "iconmanifestacion" -> DialogoReporte(
                titulo = "¿Desea reportar una manifestación?",
                iconoRes = R.drawable.iconmanifestacion,
                onDescartar = { reporteSeleccionado = null },
                onAceptar = { 
                    homeViewModel.reportarRapido("Manifestación")
                    reporteSeleccionado = null 
                }
            )
        }
    }
}
