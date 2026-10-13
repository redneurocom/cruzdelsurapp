package com.jsalinas.cruzdelsurapp.presentation.screens.inicio

import com.jsalinas.cruzdelsurapp.domain.model.Equipaje

data class InicioUiState (
    val equipajes: List<Equipaje> = emptyList(),
    val isLoading: Boolean = false
)
