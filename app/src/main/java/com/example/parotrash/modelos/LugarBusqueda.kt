package com.example.parotrash.modelos

data class LugarBusqueda(
    val nombre: String,
    val latitud: Double,
    val longitud: Double,
    val tipo: String, // "estacion" o "paradero"
    val direccion: String? = null
)