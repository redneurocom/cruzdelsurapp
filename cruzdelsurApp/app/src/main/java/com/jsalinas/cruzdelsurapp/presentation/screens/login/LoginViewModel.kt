package com.jsalinas.cruzdelsurapp.presentation.screens.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jsalinas.cruzdelsurapp.presentation.common.SnackbarType
import com.jsalinas.cruzdelsurapp.presentation.event.UiEvent
import com.jsalinas.cruzdelsurapp.presentation.event.UiEventBus
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val eventBus: UiEventBus
): ViewModel() {
    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState = _uiState.asStateFlow()

    fun onUsuarioChange(value: String) = _uiState.update { it.copy(usuario = value) }
    fun onPasswordChange(value: String) = _uiState.update { it.copy(password = value) }
    fun onTogglePassword() = _uiState.update { it.copy(passwordVisible = !it.passwordVisible) }

    fun ingresar(onSuccess: () -> Unit){
        val state = _uiState.value

        if(state.usuario.isBlank() || state.password.isBlank()){
            viewModelScope.launch {
                eventBus.sendEvent(UiEvent.ShowSnackbar("Ingrese su usuario y contraseña", SnackbarType.WARNING))
            }
            return
        }

        viewModelScope.launch {
            eventBus.sendEvent(UiEvent.ShowSnackbar("Bienvenido, ${state.usuario}", SnackbarType.SUCCESS))
        }
        onSuccess()
    }
}
