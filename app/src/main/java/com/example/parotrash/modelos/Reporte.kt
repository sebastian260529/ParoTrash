package com.example.parotrash.modelos

import com.google.firebase.firestore.IgnoreExtraProperties

@IgnoreExtraProperties
data class Reporte(
    val id: String = "",
    val id_usuario: String = "",
    val descripcion: String = "",
    val fechapublicacion: Long = 0L,
    val tipo: String = "",
    val ubicacion: List<Double> = listOf(0.0, 0.0),
    val confirmaciones: Int = 0,
    val descartes: Int = 0
)
