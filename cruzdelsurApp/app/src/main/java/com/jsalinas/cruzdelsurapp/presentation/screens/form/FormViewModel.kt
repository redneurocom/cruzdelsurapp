package com.jsalinas.cruzdelsurapp.presentation.screens.form

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jsalinas.cruzdelsurapp.domain.model.Equipaje
import com.jsalinas.cruzdelsurapp.domain.usecase.ActualizarEquipajeUseCase
import com.jsalinas.cruzdelsurapp.domain.usecase.GetDestinosUseCase
import com.jsalinas.cruzdelsurapp.domain.usecase.GetEquipajeUseCase
import com.jsalinas.cruzdelsurapp.domain.usecase.InsertarEquipajeUseCase
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
class FormViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getEquipajeUseCase: GetEquipajeUseCase,
    private val getDestinosUseCase: GetDestinosUseCase,
    private val insertarEquipajeUseCase: InsertarEquipajeUseCase,
    private val actualizarEquipajeUseCase: ActualizarEquipajeUseCase,
    private val eventBus: UiEventBus
): ViewModel(){
    private val _uiState = MutableStateFlow(FormUiState())
    val uiState = _uiState.asStateFlow()

    init {
        cargarDestinos()

        val equipajeId: String? = savedStateHandle["id"]
        if(!equipajeId.isNullOrEmpty() && equipajeId != "{id}"){
            _uiState.update { it.copy(id = equipajeId, isEditing = true) }
            cargarEquipajeData(equipajeId)
        }
    }

    private fun cargarDestinos(){
        viewModelScope.launch {
            getDestinosUseCase()
                .onSuccess { destinos ->
                    _uiState.update { it.copy(destinos = destinos) }
                }
                .onFailure { error ->
                    eventBus.sendEvent(
                        UiEvent.ShowSnackbar(
                            error.message ?: "Error al cargar los destinos", SnackbarType.ERROR
                        )
                    )
                }
        }
    }

    private fun cargarEquipajeData(id: String){
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            getEquipajeUseCase(id)
                .onSuccess { equipaje ->
                    _uiState.update {
                        it.copy(
                            boleto = equipaje.boleto,
                            pasajero = equipaje.pasajero,
                            destino = equipaje.destino,
                            tipo = equipaje.tipo,
                            peso = equipaje.peso.toString(),
                            observaciones = equipaje.observaciones,
                            isLoading = false
                        )
                    }
                }
                .onFailure {
                    _uiState.update { it.copy(isLoading = false) }
                }
        }
    }

    fun onBoletoChange(value: String) = _uiState.update { it.copy(boleto = value) }
    fun onPasajeroChange(value: String) = _uiState.update { it.copy(pasajero = value) }
    fun onDestinoChange(value: String) = _uiState.update { it.copy(destino = value) }
    fun onTipoChange(value: String) = _uiState.update { it.copy(tipo = value) }
    fun onPesoChange(value: String) = _uiState.update { it.copy(peso = value) }
    fun onObservacionesChange(value: String) = _uiState.update { it.copy(observaciones = value) }

    fun guardarEquipaje(onSucces: () -> Unit){
        val state = _uiState.value

        if(state.boleto.isBlank() || state.pasajero.isBlank() || state.destino.isBlank() || state.tipo.isBlank() || state.peso.isBlank()){
            viewModelScope.launch {
                eventBus.sendEvent(UiEvent.ShowSnackbar("Todos los campos son obligatorios",SnackbarType.WARNING))
            }
            return
        }

        val pesoInt = state.peso.toIntOrNull()
        if (pesoInt == null || pesoInt <= 0){
            viewModelScope.launch {
                eventBus.sendEvent(UiEvent.ShowSnackbar("El peso debe ser válido", SnackbarType.WARNING))
            }
            return
        }

        val equipaje = Equipaje(
            id = state.id.orEmpty(),
            boleto = state.boleto,
            pasajero = state.pasajero,
            destino = state.destino,
            tipo = state.tipo,
            peso = pesoInt,
            observaciones = state.observaciones
        )

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            val result = if(state.isEditing){
                actualizarEquipajeUseCase(state.id!!,equipaje)
            }else{
                insertarEquipajeUseCase(equipaje)
            }

            result.onSuccess {
                _uiState.update { it.copy(isLoading = false) }
                val mensaje = if (state.isEditing) "Equipaje actualizado con éxito" else "Equipaje registrado con éxito"
                eventBus.sendEvent(UiEvent.ShowSnackbar(mensaje, SnackbarType.SUCCESS))
                onSucces()
            }.onFailure { error ->
                _uiState.update { it.copy(isLoading = false) }
                eventBus.sendEvent(UiEvent.ShowSnackbar(error.message ?: "Error al guardar el equipaje", SnackbarType.ERROR))
            }
        }
    }

    fun cancelar(onSucces: () -> Unit){
        onSucces()
    }
}
