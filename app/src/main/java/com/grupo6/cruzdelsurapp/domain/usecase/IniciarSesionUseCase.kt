package com.grupo6.cruzdelsurapp.domain.usecase

import com.grupo6.cruzdelsurapp.domain.model.Usuario
import com.grupo6.cruzdelsurapp.domain.repository.AuthRepository
import javax.inject.Inject

class IniciarSesionUseCase @Inject constructor(private val repository: AuthRepository) {
    suspend operator fun invoke(codigo: String, password: String): Result<Usuario> =
        repository.iniciarSesion(codigo, password)
}
