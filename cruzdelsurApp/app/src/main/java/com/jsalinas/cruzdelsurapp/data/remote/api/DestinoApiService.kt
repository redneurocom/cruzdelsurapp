package com.jsalinas.cruzdelsurapp.data.remote.api

import com.jsalinas.cruzdelsurapp.data.remote.dto.ApiResponseDto
import com.jsalinas.cruzdelsurapp.data.remote.dto.DestinosDataDto
import retrofit2.http.GET

interface DestinoApiService {
    @GET("destinos")
    suspend fun getDestinos(): ApiResponseDto<DestinosDataDto>
}
