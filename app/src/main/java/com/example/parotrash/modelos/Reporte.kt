package com.example.parotrash.modelos

data class Reporte(
    val id: String = "",
    val id_usuario: String = "",
    val descripcion: String = "",
    val fechapublicacion: Long = System.currentTimeMillis(),
    val tipo: String = "",
    val ubicacion: List<Double> = listOf(0.0, 0.0),
)