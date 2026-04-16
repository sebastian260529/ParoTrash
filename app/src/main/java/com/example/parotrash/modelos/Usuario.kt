package com.example.parotrash.modelos

data class Usuario(
    val uid: String = "",
    val nombre: String = "",
    val correo: String = "",
    val reputacion: Int = 0,
    val reportesHechos: Int = 0,
    val fechaRegistro: Long = System.currentTimeMillis(),
)