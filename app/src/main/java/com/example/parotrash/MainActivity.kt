package com.example.parotrash

import android.Manifest
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.rememberNavController
import com.example.parotrash.data.NotificationPreferences
import com.example.parotrash.data.PermissionPreferences
import com.example.parotrash.data.SessionManager
import com.example.parotrash.ui.navegacion.NavegacionApp
import com.example.parotrash.ui.navegacion.Pantallas
import com.example.parotrash.ui.theme.ParoTrashTheme
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import kotlinx.coroutines.delay

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ParoTrashTheme {
                val notificationPreferences = remember { NotificationPreferences(this) }
                val permissionPreferences = remember { PermissionPreferences(this) }
                val sessionManager = SessionManager(this)
                ParotrashApp(
                    sessionManager = sessionManager,
                    notificationPreferences = notificationPreferences,
                    permissionPreferences = permissionPreferences
                )
            }
        }
    }
}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun ParotrashApp(
    sessionManager: SessionManager,
    notificationPreferences: NotificationPreferences,
    permissionPreferences: PermissionPreferences
) {
    val context = LocalContext.current

    val locationPermissions = rememberMultiplePermissionsState(
        permissions = listOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )
    )

    var startDestination by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(Unit) {
        delay(100)
        if (!locationPermissions.allPermissionsGranted) {
            locationPermissions.launchMultiplePermissionRequest()
        }
    }

    LaunchedEffect(locationPermissions.allPermissionsGranted) {
        if (locationPermissions.allPermissionsGranted) {
            startDestination = Pantallas.Bienvenida.ruta
        }
    }

    if (startDestination == null) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.logo),
                contentDescription = "Logo",
                modifier = Modifier.size(150.dp)
            )
        }
    } else {
        val destination = startDestination ?: Pantallas.Bienvenida.ruta
        NavegacionApp(
            navController = rememberNavController(),
            startDestination = destination,
            sessionManager = sessionManager,
            notificationPreferences = notificationPreferences,
            permissionPreferences = permissionPreferences
        )
    }
}