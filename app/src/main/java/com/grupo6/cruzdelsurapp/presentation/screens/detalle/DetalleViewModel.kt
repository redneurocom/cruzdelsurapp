package com.grupo6.cruzdelsurapp.presentation.screens.detalle

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.grupo6.cruzdelsurapp.domain.usecase.EliminarEquipajeUseCase
import com.grupo6.cruzdelsurapp.domain.usecase.GetEquipajeUseCase
import com.grupo6.cruzdelsurapp.presentation.common.SnackbarType
import com.grupo6.cruzdelsurapp.presentation.session.SesionManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetalleViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getEquipajeUseCase: GetEquipajeUseCase,
    private val eliminarEquipajeUseCase: EliminarEquipajeUseCase,
    sesionManager: SesionManager
): ViewModel() {
    var uiState by mutableStateOf(DetalleUiState())
        private set

    private val equipajeId: String? = savedStateHandle["id"]

    init {
        uiState = uiState.copy(puedeEditar = sesionManager.usuario != null)
        cargarEquipaje()
    }

    fun onMensajeMostrado() { uiState = uiState.copy(mensaje = null) }

    fun cargarEquipaje() {
        val id = equipajeId ?: return
        viewModelScope.launch {
            uiState = uiState.copy(isLoading = true)
            try {
                val equipaje = getEquipajeUseCase(id)
                uiState = uiState.copy(equipaje = equipaje, isLoading = false)
            } catch (e: Exception) {
                uiState = uiState.copy(
                    isLoading = false,
                    mensaje = e.message ?: "Error al obtener el detalle del equipaje",
                    tipoMensaje = SnackbarType.ERROR
                )
            }
        }
    }

    fun showDeleteDialog() { uiState = uiState.copy(showDeleteDialog = true) }
    fun dismissDeleteDialog() { uiState = uiState.copy(showDeleteDialog = false) }

    fun confirmDelete(onDeleted: () -> Unit) {
        val id = equipajeId ?: return
        viewModelScope.launch {
            dismissDeleteDialog()
            uiState = uiState.copy(isLoading = true)
            try {
                eliminarEquipajeUseCase(id)
                uiState = uiState.copy(isLoading = false, mensaje = "Registro de equipaje eliminado", tipoMensaje = SnackbarType.SUCCESS)
                onDeleted()
            } catch (e: Exception) {
                uiState = uiState.copy(
                    isLoading = false,
                    mensaje = e.message ?: "Error al eliminar el registro",
                    tipoMensaje = SnackbarType.ERROR
                )
            }
        }
    }
}