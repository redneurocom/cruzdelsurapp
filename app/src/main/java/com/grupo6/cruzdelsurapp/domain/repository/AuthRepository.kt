package com.grupo6.cruzdelsurapp.domain.repository

import com.grupo6.cruzdelsurapp.domain.model.Usuario

interface AuthRepository {
    suspend fun iniciarSesion(codigo: String, password: String): Result<Usuario>
}
