package com.example.parotrash.ui.pantallas

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.parotrash.R
import com.example.parotrash.ui.componentes.BotonCargando
import com.example.parotrash.ui.componentes.BotonInformacion
import com.example.parotrash.ui.componentes.Cabecera
import com.example.parotrash.ui.componentes.Formulario
import com.example.parotrash.ui.componentes.Logo
import com.example.parotrash.ui.theme.ParoTrashTheme
import com.example.parotrash.ui.viewmodel.CambiarCorreoViewModel

@Composable
fun PantallaCambiarCorreo(
    irAHome: () -> Unit,
    irAConfiguracion: () -> Unit,
    irARegistroExitoso: () -> Unit,
    irAConfirmarDescartarCambios: () -> Unit,
    viewModel: CambiarCorreoViewModel = viewModel()
) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Cabecera(
            texto = stringResource(R.string.cambiar_correo),
            onBackClick = irAConfiguracion,
            onCloseClick = irAHome
        )

        Spacer(modifier = Modifier.height(30.dp))

        Logo(modifier = Modifier.size(120.dp))

        Text(
            text = stringResource(R.string.descripcion_3),
            fontSize = 14.sp,
            color = MaterialTheme.colorScheme.outline,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(30.dp))

        Column(
            verticalArrangement = Arrangement.spacedBy(40.dp),
            modifier = Modifier.fillMaxWidth()
        ) {

            // 📧 Correo actual (desde Firebase)
            BotonInformacion(
                icon1 = Icons.Default.Email,
                texto = stringResource(R.string.correo_actual),
                valorFirebase = viewModel.correoActual,
                onClick = {}
            )

            // 📧 Nuevo correo
            Formulario(
                icon1 = Icons.Default.Email,
                nombre = stringResource(R.string.correo_nuevo),
                abajo = viewModel.error,
                icon2 = false,
                usuario = viewModel.nuevoCorreo,
                onTextChange = { viewModel.onNuevoCorreoChange(it) }
            )

            // 🔐 Contraseña (IMPORTANTE)
            Formulario(
                icon1 = Icons.Default.Lock,
                nombre = stringResource(R.string.contrase_a),
                abajo = viewModel.error,
                icon2 = true,
                usuario = viewModel.contrasena,
                onTextChange = { viewModel.onContrasenaChange(it) }
            )

            // ✅ Confirmar cambios
            BotonCargando(
                nombre = stringResource(R.string.confirmar_1),
                isLoading = viewModel.isLoading,
                onClick = {
                    viewModel.cambiarCorreo {
                        irARegistroExitoso()
                    }
                }
            )

            // ❌ Descartar cambios
            BotonCargando(
                nombre = stringResource(R.string.descartar_cambios_1),
                isLoading = false,
                onClick = {
                    viewModel.limpiar()
                    irAConfirmarDescartarCambios()
                }
            )
        }
    }
}