package com.jsalinas.cruzdelsurapp.presentation.screens.detalle

import com.jsalinas.cruzdelsurapp.domain.model.Equipaje

data class DetalleUiState(
    val equipaje: Equipaje? = null,
    val isLoading: Boolean = false,
    val showDeleteDialog: Boolean = false
)
