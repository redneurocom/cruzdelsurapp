package com.grupo6.cruzdelsurapp.data.remote.api

import com.grupo6.cruzdelsurapp.data.remote.dto.ApiResponseDto
import com.grupo6.cruzdelsurapp.data.remote.dto.DestinosDataDto
import retrofit2.http.GET

interface DestinoApiService {
    @GET("destinos")
    suspend fun getDestinos(): ApiResponseDto<DestinosDataDto>
}