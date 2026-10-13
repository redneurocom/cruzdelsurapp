package com.grupo6.cruzdelsurapp.presentation.screens.inicio

import com.grupo6.cruzdelsurapp.domain.model.Equipaje
import com.grupo6.cruzdelsurapp.presentation.common.SnackbarType

data class InicioUiState (
    val equipajes: List<Equipaje> = emptyList(),
    val isLoading: Boolean = false,
    val mensaje: String? = null,
    val tipoMensaje: SnackbarType = SnackbarType.ERROR
)