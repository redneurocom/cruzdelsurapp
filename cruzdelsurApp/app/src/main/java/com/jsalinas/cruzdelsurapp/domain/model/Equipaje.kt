package com.jsalinas.cruzdelsurapp.domain.model

data class Equipaje(
    val id: String = "",
    val boleto: String,
    val pasajero: String,
    val destino: String,
    val tipo: String,
    val peso: Int,
    val observaciones: String
)
