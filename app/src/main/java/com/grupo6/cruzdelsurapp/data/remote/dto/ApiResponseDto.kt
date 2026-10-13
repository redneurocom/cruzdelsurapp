package com.grupo6.cruzdelsurapp.data.remote.dto

data class ApiResponseDto<T>(
    val success: Boolean,
    val data: T,
    val timestamp: String?
)
