package com.grupo6.cruzdelsurapp.data.remote.dto

import com.grupo6.cruzdelsurapp.domain.model.Usuario

data class UsuarioDto(
    val codigo: String?,
    val nombre: String?,
    val rol: String?,
    val terminal: String?,
    val token: String?
) {
    fun toDomain(): Usuario {
        return Usuario(
            codigo = codigo.orEmpty(),
            nombre = nombre.orEmpty(),
            rol = rol.orEmpty(),
            terminal = terminal.orEmpty(),
            token = token.orEmpty()
        )
    }
}
