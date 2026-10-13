package com.grupo6.cruzdelsurapp.data.remote.dto

import com.grupo6.cruzdelsurapp.domain.model.Destino

data class DestinoDto(val id: String?, val nombre: String?) {
    fun toDomain(): Destino {
        return Destino(
            id = id.orEmpty(),
            nombre = nombre.orEmpty()
        )
    }
}
