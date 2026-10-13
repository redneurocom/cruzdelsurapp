package com.grupo6.cruzdelsurapp.presentation.screens.inicio

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.grupo6.cruzdelsurapp.domain.usecase.GetEquipajesUseCase
import com.grupo6.cruzdelsurapp.presentation.common.SnackbarType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class InicioViewModel @Inject constructor(
    private val getEquipajesUseCase: GetEquipajesUseCase
): ViewModel() {
    var uiState by mutableStateOf(InicioUiState())
        private set

    fun onMensajeMostrado() { uiState = uiState.copy(mensaje = null) }

    fun cargarEquipajes(){
        viewModelScope.launch {
            uiState = uiState.copy(isLoading = true)
            try {
                val equipajes = getEquipajesUseCase()
                uiState = uiState.copy(equipajes = equipajes, isLoading = false)
            } catch (e: Exception) {
                uiState = uiState.copy(
                    isLoading = false,
                    mensaje = e.message ?: "Error al cargar el manifiesto de bodega",
                    tipoMensaje = SnackbarType.ERROR
                )
            }
        }
    }
}