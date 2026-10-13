package com.jsalinas.cruzdelsurapp.presentation.screens.buscar

import com.jsalinas.cruzdelsurapp.domain.model.Equipaje

data class BuscarUiState(
    val query: String = "",
    val allEquipajes: List<Equipaje> = emptyList(),
    val filteredEquipajes: List<Equipaje> = emptyList(),
    val isLoading: Boolean = false
)
