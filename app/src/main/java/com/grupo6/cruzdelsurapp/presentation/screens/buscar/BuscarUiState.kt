package com.grupo6.cruzdelsurapp.presentation.screens.buscar

import com.grupo6.cruzdelsurapp.domain.model.Equipaje

data class BuscarUiState(
    val query: String = "",
    val allEquipajes: List<Equipaje> = emptyList(),
    val filteredEquipajes: List<Equipaje> = emptyList(),
    val isLoading: Boolean = false
)
