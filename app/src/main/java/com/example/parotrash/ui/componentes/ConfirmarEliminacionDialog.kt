package com.example.parotrash.ui.componentes

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.DialogProperties
import com.example.parotrash.R
import com.example.parotrash.data.SessionManager
import com.example.parotrash.ui.viewmodel.EliminarCuentaViewModel
import com.example.parotrash.ui.viewmodel.InicioSesionViewModel

@Composable
fun ConfirmarEliminacionDialog(
    viewModel: EliminarCuentaViewModel,
    loginViewModel: InicioSesionViewModel,
    sessionManager: SessionManager,
    onDismiss: () -> Unit,
    onEliminado: () -> Unit
) {
    var mostrarDialogoContrasena by remember { mutableStateOf(true) }

    LaunchedEffect(viewModel.verificacionExitosa) {
        if (viewModel.verificacionExitosa) {
            mostrarDialogoContrasena = false
        }
    }


    val dialogProperties = DialogProperties(
        usePlatformDefaultWidth = false
    )

    if (mostrarDialogoContrasena) {
        AlertDialog(
            onDismissRequest = onDismiss,
            title = { Text(stringResource(R.string.confirmar_acci_n)) },
            text = {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp)
                ) {
                    Text(stringResource(R.string.dato))
                    Spacer(modifier = Modifier.height(16.dp))
                    Formulario(
                        icon1 = Icons.Default.Lock,
                        nombre = "Contraseña",
                        abajo = viewModel.errorContrasena,
                        icon2 = true,
                        usuario = viewModel.contrasena,
                        onTextChange = { viewModel.actualizarContrasena(it) }
                    )
                    if (viewModel.errorGeneral != null) {
                        Text(
                            text = viewModel.errorGeneral ?: "",
                            color = MaterialTheme.colorScheme.error,
                            fontSize = 12.sp,
                            modifier = Modifier.padding(top = 8.dp)
                        )
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        viewModel.verificarContrasena { }
                    },
                    enabled = !viewModel.isLoading,
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                    modifier = Modifier
                ) {
                    if (viewModel.isLoading) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(20.dp),
                            color = MaterialTheme.colorScheme.onPrimary
                        )
                    } else {
                        Text(stringResource(R.string.verificar))
                    }
                }
            },
            dismissButton = {
                TextButton(onClick = onDismiss) {
                    Text(stringResource(R.string.cancelar_1))
                }
            },
            modifier = Modifier,
            properties = dialogProperties
        )
    } else {
        AlertDialog(
            onDismissRequest = onDismiss,
            title = { Text(stringResource(R.string.Informacion)) },
            text = {
                Text(
                    text = stringResource(R.string.Advertencia),
                    modifier = Modifier.padding(horizontal = 8.dp)
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        viewModel.eliminarCuenta(
                            sessionManager = sessionManager,
                            onSuccess = {
                                loginViewModel.resetearEstado()
                                onEliminado()
                                onDismiss()
                            },
                            onError = { error ->

                            }
                        )
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                    modifier = Modifier
                ) {
                    if (viewModel.isLoading) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(20.dp),
                            color = MaterialTheme.colorScheme.onPrimary
                        )
                    } else {
                        Text(stringResource(R.string.aceptar))
                    }
                }
            },
            dismissButton = {
                TextButton(onClick = onDismiss) {
                    Text(stringResource(R.string.cancelar))
                }
            },
            properties = dialogProperties
        )
    }
}