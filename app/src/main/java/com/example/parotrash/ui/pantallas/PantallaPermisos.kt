package com.example.parotrash.ui.pantallas

import android.Manifest
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.Settings
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.PushPin
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.parotrash.R
import com.example.parotrash.data.PermissionPreferences
import com.example.parotrash.ui.componentes.BotonConSwitch
import com.example.parotrash.ui.componentes.Cabecera
import com.example.parotrash.ui.theme.ParoTrashTheme
import com.example.parotrash.ui.viewmodel.PermisosViewModel
import com.example.parotrash.ui.viewmodel.PermisosViewModelFactory
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun PantallaPermisos(
    irAHome: () -> Unit,
    irAConfiguracion: () -> Unit,
    permissionPreferences: PermissionPreferences,
    viewModel: PermisosViewModel = viewModel(
        factory = PermisosViewModelFactory(permissionPreferences)
    )
) {
    val context = LocalContext.current

    val ubicacionPermiso = rememberPermissionState(Manifest.permission.ACCESS_FINE_LOCATION)

    val notificacionesPermiso = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        rememberPermissionState(Manifest.permission.POST_NOTIFICATIONS)
    } else null

    val almacenamientoPermiso = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        rememberPermissionState(Manifest.permission.READ_MEDIA_IMAGES)
    } else {
        rememberPermissionState(Manifest.permission.READ_EXTERNAL_STORAGE)
    }

    var ubicacionSwitch by remember { mutableStateOf(viewModel.ubicacion) }
    var notificacionesSwitch by remember { mutableStateOf(viewModel.notificaciones) }
    var almacenamientoSwitch by remember { mutableStateOf(viewModel.almacenamiento) }

    // Función para abrir configuración de la app
    fun abrirConfiguracionApp() {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
            data = Uri.fromParts("package", context.packageName, null)
        }
        context.startActivity(intent)
    }

    LaunchedEffect(ubicacionPermiso.status.isGranted) {
        val concedido = ubicacionPermiso.status.isGranted
        if (concedido != viewModel.ubicacion) {
            viewModel.actualizarUbicacion(concedido)
            ubicacionSwitch = concedido
        }
    }

    LaunchedEffect(notificacionesPermiso?.status?.isGranted) {
        val concedido = notificacionesPermiso?.status?.isGranted ?: false
        if (concedido != viewModel.notificaciones) {
            viewModel.actualizarNotificaciones(concedido)
            notificacionesSwitch = concedido
        }
    }

    LaunchedEffect(almacenamientoPermiso.status.isGranted) {
        val concedido = almacenamientoPermiso.status.isGranted
        if (concedido != viewModel.almacenamiento) {
            viewModel.actualizarAlmacenamiento(concedido)
            almacenamientoSwitch = concedido
        }
    }

    LaunchedEffect(viewModel.ubicacion) {
        if (ubicacionSwitch != viewModel.ubicacion) {
            ubicacionSwitch = viewModel.ubicacion
        }
    }
    LaunchedEffect(viewModel.notificaciones) {
        if (notificacionesSwitch != viewModel.notificaciones) {
            notificacionesSwitch = viewModel.notificaciones
        }
    }
    LaunchedEffect(viewModel.almacenamiento) {
        if (almacenamientoSwitch != viewModel.almacenamiento) {
            almacenamientoSwitch = viewModel.almacenamiento
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Cabecera(
            texto = stringResource(R.string.privacidad_1),
            onBackClick = irAConfiguracion,
            onCloseClick = irAHome
        )

        Spacer(modifier = Modifier.height(30.dp))

        Text(
            text = stringResource(R.string.descripcion_5),
            fontSize = 14.sp,
            color = MaterialTheme.colorScheme.outline
        )

        Spacer(modifier = Modifier.height(60.dp))

        Column(
            verticalArrangement = Arrangement.spacedBy(30.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            // Ubicación
            BotonConSwitch(
                icon1 = Icons.Default.PushPin,
                texto = stringResource(R.string.ubicaci_n),
                estaActivado = ubicacionSwitch,
                onCheckedChange = { activar ->
                    if (activar) {
                        ubicacionPermiso.launchPermissionRequest()
                    } else {
                        ubicacionSwitch = false
                        viewModel.actualizarUbicacion(false)
                        abrirConfiguracionApp()
                    }
                },
                descripcion = stringResource(R.string.permitir_acceso_a_tu_ubicaci_n)
            )

            // Notificaciones (solo Android 13+)
            if (notificacionesPermiso != null) {
                BotonConSwitch(
                    icon1 = Icons.Default.Notifications,
                    texto = stringResource(R.string.notificaciones_3),
                    estaActivado = notificacionesSwitch,
                    onCheckedChange = { activar ->
                        if (activar) {
                            notificacionesPermiso.launchPermissionRequest()
                        } else {
                            notificacionesSwitch = false
                            viewModel.actualizarNotificaciones(false)
                            abrirConfiguracionApp()
                        }
                    },
                    descripcion = stringResource(R.string.descripcion_6)
                )
            }

            // Almacenamiento
            BotonConSwitch(
                icon1 = Icons.Default.PushPin,
                texto = stringResource(R.string.almacenamiento),
                estaActivado = almacenamientoSwitch,
                onCheckedChange = { activar ->
                    if (activar) {
                        almacenamientoPermiso.launchPermissionRequest()
                    } else {
                        almacenamientoSwitch = false
                        viewModel.actualizarAlmacenamiento(false)
                        abrirConfiguracionApp()
                    }
                },
                descripcion = stringResource(R.string.descripcion_7)
            )
        }
    }
}