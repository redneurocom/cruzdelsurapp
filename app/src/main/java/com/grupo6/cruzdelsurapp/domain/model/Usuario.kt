package com.grupo6.cruzdelsurapp.domain.model

/** Operario con sesión iniciada (counter, bodega o supervisor). */
data class Usuario(
    val codigo: String,
    val nombre: String,
    val rol: String,
    val terminal: String,
    val token: String
)
