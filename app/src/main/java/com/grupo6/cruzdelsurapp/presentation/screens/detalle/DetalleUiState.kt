package com.grupo6.cruzdelsurapp.presentation.screens.detalle

import com.grupo6.cruzdelsurapp.domain.model.Equipaje
import com.grupo6.cruzdelsurapp.presentation.common.SnackbarType

data class DetalleUiState(
    val equipaje: Equipaje? = null,
    val isLoading: Boolean = false,
    val showDeleteDialog: Boolean = false,
    // Solo el operario con sesión puede editar o eliminar (el pasajero solo consulta)
    val puedeEditar: Boolean = false,
    val mensaje: String? = null,
    val tipoMensaje: SnackbarType = SnackbarType.ERROR
)
