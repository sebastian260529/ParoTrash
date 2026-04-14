package com.example.parotrash.ui.pantallas

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.parotrash.ui.componentes.BotonCargando
import com.example.parotrash.ui.componentes.Cabecera
import com.example.parotrash.ui.componentes.Formulario
import com.example.parotrash.ui.componentes.Logo
import com.example.parotrash.ui.theme.ParoTrashTheme
import com.example.parotrash.ui.viewmodel.CambiarContrasenaViewModel

@Composable
fun PantallaCambiarContrasena(
    irAHome: () -> Unit,
    irAConfiguracion: () -> Unit,
    irARegistroExitoso: () -> Unit,
    irAConfirmarDescartarCambios: () -> Unit,
    irARecuperarContrasena: () -> Unit,
    viewModel: CambiarContrasenaViewModel = viewModel()
) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Cabecera(
            texto = "Cambiar Contraseña",
            onBackClick = irAConfiguracion,
            onCloseClick = irAHome
        )

        Spacer(modifier = Modifier.height(30.dp))

        Logo(modifier = Modifier.size(120.dp))

        Spacer(modifier = Modifier.height(24.dp))

        Column(
            verticalArrangement = Arrangement.spacedBy(20.dp),
            modifier = Modifier.fillMaxWidth()
        ) {

            // 🔐 Contraseña actual
            Formulario(
                icon1 = Icons.Default.Lock,
                nombre = "Contraseña Actual",
                abajo = viewModel.error,
                icon2 = true,
                usuario = viewModel.contrasenaActual,
                onTextChange = { viewModel.onContrasenaActualChange(it) }
            )

            // 🔑 Nueva contraseña
            Formulario(
                icon1 = Icons.Default.Lock,
                nombre = "Contraseña Nueva",
                abajo = viewModel.error,
                icon2 = true,
                usuario = viewModel.nuevaContrasena,
                onTextChange = { viewModel.onNuevaContrasenaChange(it) }
            )

            // 🔁 Confirmar contraseña
            Formulario(
                icon1 = Icons.Default.Lock,
                nombre = "Confirmar Contraseña Nueva",
                abajo = viewModel.error,
                icon2 = true,
                usuario = viewModel.confirmarContrasena,
                onTextChange = { viewModel.onConfirmarContrasenaChange(it) }
            )

            // ✅ Confirmar cambios
            BotonCargando(
                nombre = "Confirmar Cambios",
                isLoading = viewModel.isLoading,
                onClick = {
                    viewModel.cambiarContrasena {
                        irARegistroExitoso()
                    }
                }
            )

            // ❌ Descartar cambios
            BotonCargando(
                nombre = "Descartar Cambios",
                isLoading = false,
                onClick = {
                    viewModel.limpiar()
                    irAConfirmarDescartarCambios()
                }
            )

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "¿Olvidaste tu contraseña?",
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.outline
                )
                Text(
                    text = "Recuperar contraseña",
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.secondary,
                    modifier = Modifier.clickable { irARecuperarContrasena() }
                )
            }
        }
    }
}
//as