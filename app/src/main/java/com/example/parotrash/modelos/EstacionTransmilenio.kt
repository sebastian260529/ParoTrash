package com.example.parotrash.modelos

data class EstacionTransmilenio(
    val nomEst: String,
    val ubEst: String?,
    val tipoEsta: Int,
    val latitud: Double,
    val longitud: Double
) {
    val tipoNombre: String
        get() = when (tipoEsta) {
            1 -> "Portal"
            2 -> "Zona"
            3 -> "Intermedia"
            4 -> "Corriente"
            else -> "Desconocido"
        }
}