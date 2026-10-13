package com.jsalinas.cruzdelsurapp.data.remote.dto

data class InsertarEquipajeDto(
    val boleto: String,
    val pasajero: String,
    val destino: String,
    val tipo: String,
    val peso: Int,
    val observaciones: String
)
