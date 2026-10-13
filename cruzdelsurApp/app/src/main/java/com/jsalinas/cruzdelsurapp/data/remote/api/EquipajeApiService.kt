package com.jsalinas.cruzdelsurapp.data.remote.api

import com.jsalinas.cruzdelsurapp.data.remote.dto.ApiResponseDto
import com.jsalinas.cruzdelsurapp.data.remote.dto.EquipajeDto
import com.jsalinas.cruzdelsurapp.data.remote.dto.EquipajesDataDto
import com.jsalinas.cruzdelsurapp.data.remote.dto.InsertarEquipajeDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface EquipajeApiService {
    @GET("equipajes")
    suspend fun getEquipajes(): ApiResponseDto<EquipajesDataDto>

    @GET("equipajes/{id}")
    suspend fun getEquipajeById(@Path("id") id: String): ApiResponseDto<EquipajeDto>

    @POST("equipajes")
    suspend fun insertarEquipaje(@Body equipaje: InsertarEquipajeDto): ApiResponseDto<EquipajeDto>

    @PUT("equipajes/{id}")
    suspend fun actualizarEquipaje(@Path("id") id: String, @Body equipaje: InsertarEquipajeDto): ApiResponseDto<EquipajeDto>

    @DELETE("equipajes/{id}")
    suspend fun eliminarEquipaje(@Path("id") id: String): Response<Unit>

}
