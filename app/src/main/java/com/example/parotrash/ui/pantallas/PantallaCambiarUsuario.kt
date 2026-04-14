package com.example.parotrash.ui.pantallas

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
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
import com.example.parotrash.ui.viewmodel.CambiarUsuarioViewModel

@Composable
fun PantallaCambiarUsuario(
    irAHome: () -> Unit,
    irAConfiguracion: () -> Unit,
    irARegistroExitoso: () -> Unit,
    irAConfirmarDescartarCambios: () -> Unit,
    viewModel: CambiarUsuarioViewModel = viewModel()
) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Cabecera(
            texto = stringResource(R.string.cambiar_usuario),
            onBackClick = irAConfiguracion,
            onCloseClick = irAHome
        )

        Spacer(modifier = Modifier.height(30.dp))

        Logo(modifier = Modifier.size(120.dp))

        Text(
            text = stringResource(R.string.descripcion_4),
            fontSize = 14.sp,
            color = MaterialTheme.colorScheme.outline,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(30.dp))

        Column(
            verticalArrangement = Arrangement.spacedBy(40.dp),
            modifier = Modifier.fillMaxWidth()
        ) {

            // 👤 Usuario actual
            BotonInformacion(
                icon1 = Icons.Default.AccountCircle,
                texto = stringResource(R.string.usuario_actual),
                valorFirebase = viewModel.usuarioActual,
                onClick = {}
            )

            // ✏️ Usuario nuevo
            Formulario(
                icon1 = Icons.Default.AccountCircle,
                nombre = stringResource(R.string.usuario_nuevo),
                abajo = viewModel.error,
                icon2 = false,
                usuario = viewModel.nuevoUsuario,
                onTextChange = { viewModel.onNuevoUsuarioChange(it) }
            )

            // ✅ Confirmar cambios
            BotonCargando(
                nombre = stringResource(R.string.confirmar_cambios_1),
                isLoading = viewModel.isLoading,
                onClick = {
                    viewModel.cambiarUsuario {
                        irARegistroExitoso()
                    }
                }
            )

            // ❌ Descartar cambios
            BotonCargando(
                nombre = stringResource(R.string.descartar_cambios_2),
                isLoading = false,
                onClick = {
                    viewModel.limpiar()
                    irAConfirmarDescartarCambios()
                }
            )
        }
    }
}
