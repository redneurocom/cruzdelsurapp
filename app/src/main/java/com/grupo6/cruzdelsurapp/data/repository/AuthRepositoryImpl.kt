package com.grupo6.cruzdelsurapp.data.repository

import com.grupo6.cruzdelsurapp.data.remote.api.AuthApiService
import com.grupo6.cruzdelsurapp.data.remote.dto.LoginRequestDto
import com.grupo6.cruzdelsurapp.domain.model.Usuario
import com.grupo6.cruzdelsurapp.domain.repository.AuthRepository
import retrofit2.HttpException
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val apiAuth: AuthApiService
) : AuthRepository {
    override suspend fun iniciarSesion(codigo: String, password: String): Usuario {
        try {
            val response = apiAuth.login(LoginRequestDto(codigo = codigo, password = password))
            if (response.success && response.data != null && !response.data.token.isNullOrBlank()) {
                return response.data.toDomain()
            } else {
                throw Exception("No se pudo iniciar sesión")
            }
        } catch (e: HttpException) {
            throw Exception(
                when (e.code()) {
                    401 -> "Código de colaborador o contraseña incorrectos"
                    400 -> "Ingrese su código de colaborador y contraseña"
                    else -> "Error del servidor al iniciar sesión (${e.code()})"
                }
            )
        }
    }
}
