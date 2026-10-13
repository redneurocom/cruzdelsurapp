package com.jsalinas.cruzdelsurapp.presentation.screens.detalle

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jsalinas.cruzdelsurapp.domain.usecase.EliminarEquipajeUseCase
import com.jsalinas.cruzdelsurapp.domain.usecase.GetEquipajeUseCase
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
class DetalleViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getEquipajeUseCase: GetEquipajeUseCase,
    private val eliminarEquipajeUseCase: EliminarEquipajeUseCase,
    private val eventBus: UiEventBus
): ViewModel() {
    private val _uiState = MutableStateFlow(DetalleUiState())
    val uiState = _uiState.asStateFlow()

    private val equipajeId: String? = savedStateHandle["id"]

    init {
        cargarEquipaje()
    }

    fun cargarEquipaje() {
        val id = equipajeId ?: return
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            getEquipajeUseCase(id)
                .onSuccess { equipaje ->
                    _uiState.update { it.copy(equipaje = equipaje, isLoading = false) }
                }
                .onFailure { error ->
                    _uiState.update { it.copy(isLoading = false) }
                    eventBus.sendEvent(
                        UiEvent.ShowSnackbar(
                            error.message ?: "Error al obtener detalle",
                            SnackbarType.ERROR
                        )
                    )
                }
        }
    }

    fun showDeleteDialog() = _uiState.update { it.copy(showDeleteDialog = true) }
    fun dismissDeleteDialog() = _uiState.update { it.copy(showDeleteDialog = false) }

    fun confirmDelete(onDeleted: () -> Unit) {
        val id = equipajeId ?: return
        viewModelScope.launch {
            dismissDeleteDialog()
            _uiState.update { it.copy(isLoading = true) }
            eliminarEquipajeUseCase(id)
                .onSuccess {
                    _uiState.update { it.copy(isLoading = false) }
                    eventBus.sendEvent(
                        UiEvent.ShowSnackbar("Equipaje eliminado con éxito", SnackbarType.SUCCESS)
                    )
                    onDeleted()
                }
                .onFailure { error ->
                    _uiState.update { it.copy(isLoading = false) }
                    eventBus.sendEvent(
                        UiEvent.ShowSnackbar(
                            error.message ?: "Error al eliminar el equipaje",
                            SnackbarType.ERROR
                        )
                    )
                }
        }
    }
}
