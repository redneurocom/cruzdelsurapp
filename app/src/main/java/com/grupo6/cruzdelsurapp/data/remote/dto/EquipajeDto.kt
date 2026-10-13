package com.grupo6.cruzdelsurapp.data.remote.dto

import com.grupo6.cruzdelsurapp.domain.model.Equipaje

data class EquipajeDto (
    val id: String?,
    val pasajero: String?,
    val boleto: String?,
    val pesoKg: Int?,
    val destino: String?,
    val observaciones: String?
){
    fun toDomain(): Equipaje {
        return Equipaje(
            id = id.orEmpty(),
            pasajero = pasajero.orEmpty(),
            boleto = boleto.orEmpty(),
            pesoKg = pesoKg ?: 0,
            destino = destino.orEmpty(),
            observaciones = observaciones.orEmpty()
        )
    }
}