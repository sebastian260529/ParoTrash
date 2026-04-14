package com.example.parotrash.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class MiCuentaViewModel : ViewModel() {

    private val auth = FirebaseAuth.getInstance()
    private val db = FirebaseFirestore.getInstance()

    var usuario by mutableStateOf("")
        private set

    var correo by mutableStateOf("")
        private set

    init {
        cargarDatos()
    }

    private fun cargarDatos() {
        val user = auth.currentUser

        if (user != null) {
            correo = user.email ?: ""
            val uid = user.uid

            db.collection("usuarios")
                .document(uid)
                .get()
                .addOnSuccessListener {
                    usuario = it.getString("usuario") ?: ""
                }
        }
    }
}