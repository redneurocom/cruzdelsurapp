package com.grupo6.cruzdelsurapp.presentation.screens.login

data class LoginUiState(
    val codigo: String = "",
    val password: String = "",
    val passwordVisible: Boolean = false,
    val isLoading: Boolean = false
)
