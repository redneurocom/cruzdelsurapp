package com.grupo6.cruzdelsurapp.domain.model

data class Equipaje(
    val id: String = "",
    val pasajero: String,
    val boleto: String,
    val pesoKg: Int,
    val destino: String,
    val observaciones: String
)
