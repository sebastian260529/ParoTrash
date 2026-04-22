package com.example.parotrash.modelos

import com.google.firebase.Timestamp
import com.google.firebase.firestore.Exclude
import com.google.firebase.firestore.IgnoreExtraProperties

@IgnoreExtraProperties
data class Reporte(
    val id: String = "",
    val id_usuario: String = "",
    val descripcion: String = "",
    val fechapublicacion: Any? = null, // Puede ser Long o Timestamp
    val tipo: String = "",
    val ubicacion: List<Double> = listOf(0.0, 0.0),
) {
    @Exclude
    fun getFechaMillis(): Long {
        return when (fechapublicacion) {
            is Long -> fechapublicacion
            is Timestamp -> fechapublicacion.toDate().time
            else -> System.currentTimeMillis()
        }
    }
}