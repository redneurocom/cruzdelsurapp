package com.jsalinas.cruzdelsurapp.presentation.screens.inicio

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jsalinas.cruzdelsurapp.domain.usecase.GetEquipajesUseCase
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
class InicioViewModel @Inject constructor(
    private val getEquipajesUseCase: GetEquipajesUseCase,
    private val eventBus: UiEventBus
): ViewModel() {
    private val _uiState = MutableStateFlow(InicioUiState())
    val uiState = _uiState.asStateFlow()

    fun cargarEquipajes(){
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            getEquipajesUseCase()
            .onSuccess { equipajes ->
                _uiState.update { it.copy(equipajes = equipajes, isLoading = false) }
            }
            .onFailure { error ->
                _uiState.update { it.copy(isLoading = false) }
                eventBus.sendEvent(
                    UiEvent.ShowSnackbar(
                        error.message ?: "Error al cargar equipajes",
                        SnackbarType.ERROR
                    )
                )
            }
        }
    }
}
