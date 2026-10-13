package com.grupo6.cruzdelsurapp.presentation.screens.inicio

import com.grupo6.cruzdelsurapp.domain.model.Equipaje

data class InicioUiState (
    val equipajes: List<Equipaje> = emptyList(),
    val isLoading: Boolean = false
)