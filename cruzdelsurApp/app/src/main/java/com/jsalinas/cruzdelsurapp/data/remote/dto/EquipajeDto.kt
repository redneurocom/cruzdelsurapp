package com.jsalinas.cruzdelsurapp.data.remote.dto

import com.jsalinas.cruzdelsurapp.domain.model.Equipaje

data class EquipajeDto (
    val id: String?,
    val boleto: String?,
    val pasajero: String?,
    val destino: String?,
    val tipo: String?,
    val peso: Int?,
    val observaciones: String?
){
    fun toDomain(): Equipaje {
        return Equipaje(
            id = id.orEmpty(),
            boleto = boleto.orEmpty(),
            pasajero = pasajero.orEmpty(),
            destino = destino.orEmpty(),
            tipo = tipo.orEmpty(),
            peso = peso ?: 0,
            observaciones = observaciones.orEmpty()
        )
    }
}
