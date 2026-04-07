package com.example.parotrash

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.parotrash.navegacion.NavegacionApp
import com.example.parotrash.ui.viewmodel.InicioSesionViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val viewModel = InicioSesionViewModel()
            NavegacionApp(viewModel = viewModel)
        }
    }
}