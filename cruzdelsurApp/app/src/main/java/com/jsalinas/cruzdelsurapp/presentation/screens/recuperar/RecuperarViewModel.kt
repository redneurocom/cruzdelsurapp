package com.jsalinas.cruzdelsurapp.presentation.screens.recuperar

import android.util.Patterns
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
class RecuperarViewModel @Inject constructor(
    private val eventBus: UiEventBus
): ViewModel() {
    private val _uiState = MutableStateFlow(RecuperarUiState())
    val uiState = _uiState.asStateFlow()

    fun onCorreoChange(value: String) = _uiState.update { it.copy(correo = value, enviado = false) }

    fun enviarInstrucciones(){
        val correo = _uiState.value.correo

        if(correo.isBlank() || !Patterns.EMAIL_ADDRESS.matcher(correo).matches()){
            viewModelScope.launch {
                eventBus.sendEvent(UiEvent.ShowSnackbar("Ingrese un correo institucional válido", SnackbarType.WARNING))
            }
            return
        }

        _uiState.update { it.copy(enviado = true) }
        viewModelScope.launch {
            eventBus.sendEvent(UiEvent.ShowSnackbar("Instrucciones enviadas a $correo", SnackbarType.SUCCESS))
        }
    }
}
