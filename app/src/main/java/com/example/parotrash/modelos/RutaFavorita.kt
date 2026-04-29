package com.example.parotrash.modelos

import com.google.firebase.firestore.IgnoreExtraProperties

@IgnoreExtraProperties
data class RutaFavorita(
    val id: String = "",
    val id_usuario: String = "",
    val nombre: String = "",
    val desdeNombre: String = "",
    val desdeLat: Double = 0.0,
    val desdeLng: Double = 0.0,
    val hastaNombre: String = "",
    val hastaLat: Double = 0.0,
    val hastaLng: Double = 0.0,
    val fechaCreacion: Long = System.currentTimeMillis()
)