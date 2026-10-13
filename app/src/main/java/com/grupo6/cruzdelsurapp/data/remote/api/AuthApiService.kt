package com.grupo6.cruzdelsurapp.data.remote.api

import com.grupo6.cruzdelsurapp.data.remote.dto.ApiResponseDto
import com.grupo6.cruzdelsurapp.data.remote.dto.LoginRequestDto
import com.grupo6.cruzdelsurapp.data.remote.dto.UsuarioDto
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApiService {
    @POST("auth/login")
    suspend fun login(@Body request: LoginRequestDto): ApiResponseDto<UsuarioDto>
}
