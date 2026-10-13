package com.grupo6.cruzdelsurapp.presentation.screens.form

import com.grupo6.cruzdelsurapp.domain.model.Destino
import com.grupo6.cruzdelsurapp.presentation.common.SnackbarType

data class FormUiState(
    val id: String? = null,
    val pasajero: String = "",
    val boleto: String = "",
    val pesoKg: String = "",
    val destino: String = "",
    val observaciones: String = "",
    val destinos: List<Destino> = emptyList(),
    val isLoading: Boolean = false,
    val isEditing: Boolean = false,
    val mensaje: String? = null,
    val tipoMensaje: SnackbarType = SnackbarType.ERROR
)
