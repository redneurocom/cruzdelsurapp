package com.grupo6.cruzdelsurapp.presentation.screens.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.grupo6.cruzdelsurapp.domain.usecase.IniciarSesionUseCase
import com.grupo6.cruzdelsurapp.presentation.common.SnackbarType
import com.grupo6.cruzdelsurapp.presentation.event.UiEvent
import com.grupo6.cruzdelsurapp.presentation.event.UiEventBus
import com.grupo6.cruzdelsurapp.presentation.session.SesionManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val iniciarSesionUseCase: IniciarSesionUseCase,
    private val sesionManager: SesionManager,
    private val eventBus: UiEventBus
) : ViewModel() {
    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState = _uiState.asStateFlow()

    fun onCodigoChange(value: String) = _uiState.update { it.copy(codigo = value.uppercase().trim()) }
    fun onPasswordChange(value: String) = _uiState.update { it.copy(password = value) }
    fun onTogglePasswordVisible() = _uiState.update { it.copy(passwordVisible = !it.passwordVisible) }

    fun iniciarSesion(onSuccess: () -> Unit) {
        val state = _uiState.value
        if (state.codigo.isBlank() || state.password.isBlank()) {
            viewModelScope.launch {
                eventBus.sendEvent(UiEvent.ShowSnackbar("Ingrese su código de colaborador y contraseña", SnackbarType.WARNING))
            }
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            iniciarSesionUseCase(state.codigo, state.password)
                .onSuccess { usuario ->
                    sesionManager.iniciarSesion(usuario)
                    _uiState.update { LoginUiState() }
                    eventBus.sendEvent(UiEvent.ShowSnackbar("Bienvenido, ${usuario.nombre}", SnackbarType.SUCCESS))
                    onSuccess()
                }
                .onFailure { error ->
                    _uiState.update { it.copy(isLoading = false, password = "") }
                    eventBus.sendEvent(
                        UiEvent.ShowSnackbar(error.message ?: "Error al iniciar sesión", SnackbarType.ERROR)
                    )
                }
        }
    }

    fun recuperarPassword() {
        viewModelScope.launch {
            eventBus.sendEvent(
                UiEvent.ShowSnackbar(
                    "Solicite el restablecimiento de su contraseña al supervisor de su terminal",
                    SnackbarType.INFO
                )
            )
        }
    }
}
