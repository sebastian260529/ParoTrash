package com.example.parotrash.ui.pantallas

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.parotrash.R
import com.example.parotrash.data.SessionManager
import com.example.parotrash.ui.componentes.BotonCargando
import com.example.parotrash.ui.componentes.BotonSimple
import com.example.parotrash.ui.componentes.Check
import com.example.parotrash.ui.viewmodel.InicioSesionViewModel

@Composable
fun PantallaHome(
    viewModel: InicioSesionViewModel,
    irACerrarSesion: () -> Unit,
    irAConfiguracion: () -> Unit,
    sessionManager: SessionManager
) {
    var cerrandoSesion by remember { mutableStateOf(false) }

    LaunchedEffect(cerrandoSesion) {
        if (cerrandoSesion) {
            viewModel.cerrarSesion(sessionManager) {
                irACerrarSesion()
            }
            cerrandoSesion = false
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(horizontal = 24.dp, vertical = 48.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Check(modifier = Modifier.size(80.dp))

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = stringResource(R.string.inicio_de_sesi_n_exitoso),
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(32.dp))

        BotonCargando(
            nombre = stringResource(R.string.cerrar_sesi_n),
            isLoading = cerrandoSesion,
            onClick = {
                cerrandoSesion = true
            }
        )

        Spacer(modifier = Modifier.height(16.dp))

        BotonSimple(
            texto = stringResource(R.string.configuraci_n_1),
            onClick = {
                irAConfiguracion()
            }
        )
    }
}