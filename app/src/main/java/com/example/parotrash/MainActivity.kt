package com.example.parotrash

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.rememberNavController
import com.example.parotrash.data.SessionManager
import com.example.parotrash.ui.navegacion.NavegacionApp
import com.example.parotrash.ui.navegacion.Pantallas
import kotlinx.coroutines.delay

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val sessionManager = SessionManager(this)
            ParotrashApp(sessionManager = sessionManager)
        }
    }
}

@Composable
fun ParotrashApp(sessionManager: SessionManager) {
    val isLoggedIn by sessionManager.isLoggedIn.collectAsState(initial = false)
    val isFirstTime by sessionManager.isFirstTime.collectAsState(initial = true)

    var startDestination by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(Unit) {
        delay(100)
        startDestination = when {
            isFirstTime -> Pantallas.Bienvenida.ruta
            isLoggedIn -> Pantallas.Home.ruta
            else -> Pantallas.InicioSesion.ruta
        }
    }

    if (startDestination == null) {

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFFFFFFF)),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.logo),
                contentDescription = "Logo",
                modifier = Modifier.size(150.dp)
            )
        }
    } else {
        NavegacionApp(
            navController = rememberNavController(),
            startDestination = startDestination!!,
            sessionManager = sessionManager
        )
    }
}