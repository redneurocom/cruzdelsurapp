package com.jsalinas.cruzdelsurapp.presentation.screens.form

import com.jsalinas.cruzdelsurapp.domain.model.Destino

data class FormUiState(
    val id: String? = null,
    val boleto: String = "",
    val pasajero: String = "",
    val destino: String = "",
    val tipo: String = "",
    val peso: String = "",
    val observaciones: String = "",
    val destinos: List<Destino> = emptyList(),
    val tipos: List<String> = listOf("Maleta", "Caja / Encomienda", "Mochila / Bolso"),
    val isLoading: Boolean = false,
    val isEditing: Boolean = false
)
