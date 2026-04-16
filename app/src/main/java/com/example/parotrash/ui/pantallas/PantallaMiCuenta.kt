// PantallaMiCuenta.kt (modificada)
package com.example.parotrash.ui.pantallas

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.parotrash.R
import com.example.parotrash.data.SessionManager
import com.example.parotrash.ui.componentes.BotonCargando
import com.example.parotrash.ui.componentes.BotonInformacion
import com.example.parotrash.ui.componentes.Cabecera
import com.example.parotrash.ui.componentes.ConfirmarEliminacionDialog
import com.example.parotrash.ui.componentes.Logo
import com.example.parotrash.ui.theme.ParoTrashTheme
import com.example.parotrash.ui.viewmodel.EliminarCuentaViewModel
import com.example.parotrash.ui.viewmodel.InicioSesionViewModel
import com.example.parotrash.ui.viewmodel.MiCuentaViewModel

@Composable
fun PantallaMiCuenta(
    viewModel: MiCuentaViewModel = viewModel(),
    loginViewModel: InicioSesionViewModel,
    irACambiarUsuario: () -> Unit,
    irACambiarCorreo: () -> Unit,
    irACambiarContrasena: () -> Unit,
    irAHome: () -> Unit,
    irAConfiguracion: () -> Unit,
    irAinicoSesion: () -> Unit,
    modifier: Modifier = Modifier,
    sessionManager: SessionManager,
) {
    // Estado para mostrar el diálogo de confirmación
    var mostrarDialogoEliminar by remember { mutableStateOf(false) }
    val eliminarViewModel: EliminarCuentaViewModel = viewModel()

    // Mostrar el diálogo si la variable está en true
    if (mostrarDialogoEliminar) {
        ConfirmarEliminacionDialog(
            viewModel = eliminarViewModel,
            loginViewModel = loginViewModel,
            sessionManager = sessionManager,  // ← pasar aquí también
            onDismiss = { mostrarDialogoEliminar = false },
            onEliminado = {
                irAinicoSesion()
            }
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Cabecera(
            texto = stringResource(R.string.mi_cuenta_1),
            onBackClick = irAConfiguracion,
            onCloseClick = irAHome
        )

        Logo(modifier = Modifier.size(160.dp))

        Spacer(modifier = Modifier.height(60.dp))

        Column(
            verticalArrangement = Arrangement.spacedBy(40.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            BotonInformacion(
                icon1 = Icons.Default.AccountCircle,
                texto = stringResource(R.string.nombre_de_usuario),
                valorFirebase = viewModel.usuario,
                onClick = irACambiarUsuario
            )

            BotonInformacion(
                icon1 = Icons.Default.Email,
                texto = stringResource(R.string.correo_electronico),
                valorFirebase = viewModel.correo,
                onClick = irACambiarCorreo
            )

            BotonInformacion(
                icon1 = Icons.Default.Lock,
                texto = stringResource(R.string.contrase_a_2),
                onClick = irACambiarContrasena
            )

            BotonCargando(
                icon1 = Icons.AutoMirrored.Filled.ExitToApp,
                isLoading = false,
                onClick = irAinicoSesion,
                nombre = stringResource(R.string.cerrar_sesi_n_2)
            )

            BotonCargando(
                isLoading = false,
                onClick = { mostrarDialogoEliminar = true },
                nombre = stringResource(R.string.eliminar_cuenta)
            )
        }
    }
}