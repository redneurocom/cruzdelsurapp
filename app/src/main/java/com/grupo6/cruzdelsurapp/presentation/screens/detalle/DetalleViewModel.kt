package com.grupo6.cruzdelsurapp.presentation.screens.detalle

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.grupo6.cruzdelsurapp.domain.usecase.EliminarEquipajeUseCase
import com.grupo6.cruzdelsurapp.domain.usecase.GetEquipajeUseCase
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
class DetalleViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getEquipajeUseCase: GetEquipajeUseCase,
    private val eliminarEquipajeUseCase: EliminarEquipajeUseCase,
    private val eventBus: UiEventBus,
    sesionManager: SesionManager
): ViewModel() {
    private val _uiState = MutableStateFlow(DetalleUiState())
    val uiState = _uiState.asStateFlow()

    private val equipajeId: String? = savedStateHandle["id"]

    init {
        _uiState.update { it.copy(puedeEditar = sesionManager.usuario.value != null) }
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
                            error.message ?: "Error al obtener el detalle del equipaje",
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
                        UiEvent.ShowSnackbar("Registro de equipaje eliminado", SnackbarType.SUCCESS)
                    )
                    onDeleted()
                }
                .onFailure { error ->
                    _uiState.update { it.copy(isLoading = false) }
                    eventBus.sendEvent(
                        UiEvent.ShowSnackbar(
                            error.message ?: "Error al eliminar el registro",
                            SnackbarType.ERROR
                        )
                    )
                }
        }
    }
}