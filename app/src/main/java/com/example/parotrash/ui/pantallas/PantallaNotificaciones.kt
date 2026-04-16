package com.example.parotrash.ui.pantallas

import android.content.Intent
import android.net.Uri
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
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.EmojiPeople
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import com.example.parotrash.data.NotificationPreferences
import com.example.parotrash.ui.componentes.BotonConSwitch
import com.example.parotrash.ui.componentes.Cabecera
import com.example.parotrash.ui.theme.ParoTrashTheme
import com.example.parotrash.ui.viewmodel.NotificacionesViewModel
import com.example.parotrash.ui.viewmodel.NotificacionesViewModelFactory

@Composable
fun PantallaNotificaciones(
    irAHome: () -> Unit,
    irAConfiguracion: () -> Unit,
    notificationPreferences: NotificationPreferences,
    viewModel: NotificacionesViewModel = viewModel(
        factory = NotificacionesViewModelFactory(notificationPreferences)
    )
) {
    val context = LocalContext.current
    val recibirAlertas = viewModel.recibirAlertas
    val bloqueosViales = viewModel.bloqueosViales
    val manifestaciones = viewModel.manifestaciones
    val rutasAlternativas = viewModel.rutasAlternativas
    val comunidad = viewModel.comunidad

    // Función para abrir configuración de notificaciones de la app
    fun abrirConfiguracionNotificaciones() {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
            data = Uri.fromParts("package", context.packageName, null)
        }
        context.startActivity(intent)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Cabecera(
            texto = stringResource(R.string.notificaciones_1),
            onBackClick = irAConfiguracion,
            onCloseClick = irAHome
        )

        Spacer(modifier = Modifier.height(30.dp))

        // Switch "Recibir alertas" - al desactivar abre configuración
        BotonConSwitch(
            icon1 = Icons.Default.Notifications,
            texto = stringResource(R.string.recibir_alertas),
            estaActivado = recibirAlertas,
            onCheckedChange = { activar ->
                if (activar) {
                    viewModel.actualizarRecibirAlertas(true)
                } else {
                    viewModel.actualizarRecibirAlertas(false)
                    abrirConfiguracionNotificaciones()  // ← Abre configuración al desactivar
                }
            }
        )

        Spacer(modifier = Modifier.height(60.dp))

        Column(
            verticalArrangement = Arrangement.spacedBy(40.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = stringResource(R.string.notificaciones_2),
                fontSize = 22.sp,
                color = MaterialTheme.colorScheme.outline
            )

            BotonConSwitch(
                icon1 = Icons.Default.DirectionsCar,
                texto = stringResource(R.string.bloqueos_viales_1),
                estaActivado = bloqueosViales,
                onCheckedChange = { viewModel.actualizarBloqueosViales(it) },
                habilitado = recibirAlertas
            )

            BotonConSwitch(
                icon1 = Icons.Default.EmojiPeople,
                texto = stringResource(R.string.manifestaciones_1),
                estaActivado = manifestaciones,
                onCheckedChange = { viewModel.actualizarManifestaciones(it) },
                habilitado = recibirAlertas
            )

            BotonConSwitch(
                icon1 = Icons.Default.Warning,
                texto = stringResource(R.string.rutas_alternativas_1),
                estaActivado = rutasAlternativas,
                onCheckedChange = { viewModel.actualizarRutasAlternativas(it) },
                habilitado = recibirAlertas
            )

            BotonConSwitch(
                icon1 = Icons.Default.People,
                texto = stringResource(R.string.comunidad),
                estaActivado = comunidad,
                onCheckedChange = { viewModel.actualizarComunidad(it) },
                habilitado = recibirAlertas
            )
        }
    }
}