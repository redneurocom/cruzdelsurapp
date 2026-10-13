package com.grupo6.cruzdelsurapp.presentation.screens.form

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.grupo6.cruzdelsurapp.domain.model.Equipaje
import com.grupo6.cruzdelsurapp.domain.usecase.ActualizarEquipajeUseCase
import com.grupo6.cruzdelsurapp.domain.usecase.GetDestinosUseCase
import com.grupo6.cruzdelsurapp.domain.usecase.GetEquipajeUseCase
import com.grupo6.cruzdelsurapp.domain.usecase.RegistrarEquipajeUseCase
import com.grupo6.cruzdelsurapp.presentation.common.SnackbarType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FormViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getEquipajeUseCase: GetEquipajeUseCase,
    private val getDestinosUseCase: GetDestinosUseCase,
    private val registrarEquipajeUseCase: RegistrarEquipajeUseCase,
    private val actualizarEquipajeUseCase: ActualizarEquipajeUseCase
): ViewModel(){
    var uiState by mutableStateOf(FormUiState())
        private set

    init {
        cargarDestinos()

        val equipajeId: String? = savedStateHandle["id"]
        if(!equipajeId.isNullOrEmpty() && equipajeId != "{id}"){
            uiState = uiState.copy(id = equipajeId, isEditing = true)
            cargarEquipajeData(equipajeId)
        }
    }

    fun onMensajeMostrado() { uiState = uiState.copy(mensaje = null) }

    private fun cargarDestinos(){
        viewModelScope.launch {
            try {
                val destinos = getDestinosUseCase()
                uiState = uiState.copy(destinos = destinos)
            } catch (e: Exception) {
                uiState = uiState.copy(mensaje = e.message ?: "Error al cargar los destinos", tipoMensaje = SnackbarType.ERROR)
            }
        }
    }

    private fun cargarEquipajeData(id: String){
        viewModelScope.launch {
            uiState = uiState.copy(isLoading = true)
            try {
                val equipaje = getEquipajeUseCase(id)
                uiState = uiState.copy(
                    pasajero = equipaje.pasajero,
                    boleto = equipaje.boleto,
                    pesoKg = equipaje.pesoKg.toString(),
                    destino = equipaje.destino,
                    observaciones = equipaje.observaciones,
                    isLoading = false
                )
            } catch (e: Exception) {
                uiState = uiState.copy(
                    isLoading = false,
                    mensaje = e.message ?: "Error al cargar el equipaje a editar",
                    tipoMensaje = SnackbarType.ERROR
                )
            }
        }
    }

    fun onPasajeroChange(value: String) { uiState = uiState.copy(pasajero = value) }
    fun onBoletoChange(value: String) { uiState = uiState.copy(boleto = value) }
    fun onPesoChange(value: String) { uiState = uiState.copy(pesoKg = value) }
    fun onDestinoChange(value: String) { uiState = uiState.copy(destino = value) }
    fun onObservacionesChange(value: String) { uiState = uiState.copy(observaciones = value) }

    fun guardarEquipaje(onSucces: () -> Unit){
        if(uiState.pasajero.isBlank() || uiState.boleto.isBlank() || uiState.pesoKg.isBlank() || uiState.destino.isBlank() || uiState.observaciones.isBlank()){
            uiState = uiState.copy(mensaje = "Todos los campos son obligatorios", tipoMensaje = SnackbarType.WARNING)
            return
        }

        val pesoInt = uiState.pesoKg.toIntOrNull()
        if (pesoInt == null || pesoInt <= 0){
            uiState = uiState.copy(mensaje = "El peso debe ser un valor válido en kg", tipoMensaje = SnackbarType.WARNING)
            return
        }

        val equipaje = Equipaje(
            id = uiState.id.orEmpty(),
            pasajero = uiState.pasajero,
            boleto = uiState.boleto,
            pesoKg = pesoInt,
            destino = uiState.destino,
            observaciones = uiState.observaciones
        )

        viewModelScope.launch {
            val editando = uiState.isEditing
            uiState = uiState.copy(isLoading = true)
            try {
                if(editando){
                    actualizarEquipajeUseCase(uiState.id!!, equipaje)
                }else{
                    registrarEquipajeUseCase(equipaje)
                }
                val mensaje = if (editando) "Registro de equipaje actualizado" else "Equipaje registrado con éxito"
                uiState = uiState.copy(isLoading = false, mensaje = mensaje, tipoMensaje = SnackbarType.SUCCESS)
                onSucces()
            } catch (e: Exception) {
                uiState = uiState.copy(
                    isLoading = false,
                    mensaje = e.message ?: "Error al registrar el equipaje",
                    tipoMensaje = SnackbarType.ERROR
                )
            }
        }
    }

    fun cancelar(onSucces: () -> Unit){
        onSucces()
    }
}