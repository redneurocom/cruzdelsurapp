package com.grupo6.cruzdelsurapp.presentation.screens.login

import com.grupo6.cruzdelsurapp.presentation.common.SnackbarType

data class LoginUiState(
    val codigo: String = "",
    val password: String = "",
    val passwordVisible: Boolean = false,
    val isLoading: Boolean = false,
    val mensaje: String? = null,
    val tipoMensaje: SnackbarType = SnackbarType.ERROR
)
