package com.grupo6.cruzdelsurapp.presentation.screens.login

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.grupo6.cruzdelsurapp.domain.usecase.IniciarSesionUseCase
import com.grupo6.cruzdelsurapp.presentation.common.SnackbarType
import com.grupo6.cruzdelsurapp.presentation.session.SesionManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val iniciarSesionUseCase: IniciarSesionUseCase,
    private val sesionManager: SesionManager
) : ViewModel() {
    var uiState by mutableStateOf(LoginUiState())
        private set

    fun onCodigoChange(value: String) { uiState = uiState.copy(codigo = value.uppercase().trim()) }
    fun onPasswordChange(value: String) { uiState = uiState.copy(password = value) }
    fun onTogglePasswordVisible() { uiState = uiState.copy(passwordVisible = !uiState.passwordVisible) }
    fun onMensajeMostrado() { uiState = uiState.copy(mensaje = null) }

    fun iniciarSesion(onSuccess: () -> Unit) {
        if (uiState.codigo.isBlank() || uiState.password.isBlank()) {
            uiState = uiState.copy(mensaje = "Ingrese su código de colaborador y contraseña", tipoMensaje = SnackbarType.WARNING)
            return
        }

        viewModelScope.launch {
            uiState = uiState.copy(isLoading = true)
            try {
                val usuario = iniciarSesionUseCase(uiState.codigo, uiState.password)
                sesionManager.iniciarSesion(usuario)
                uiState = LoginUiState(mensaje = "Bienvenido, ${usuario.nombre}", tipoMensaje = SnackbarType.SUCCESS)
                onSuccess()
            } catch (e: Exception) {
                uiState = uiState.copy(
                    isLoading = false,
                    password = "",
                    mensaje = e.message ?: "Error al iniciar sesión",
                    tipoMensaje = SnackbarType.ERROR
                )
            }
        }
    }
}
