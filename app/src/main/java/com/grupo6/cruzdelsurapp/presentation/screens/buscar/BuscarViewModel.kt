package com.grupo6.cruzdelsurapp.presentation.screens.buscar

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.grupo6.cruzdelsurapp.domain.model.Equipaje
import com.grupo6.cruzdelsurapp.domain.usecase.GetEquipajesUseCase
import com.grupo6.cruzdelsurapp.presentation.common.SnackbarType
import com.grupo6.cruzdelsurapp.presentation.common.codigoRastreo
import com.grupo6.cruzdelsurapp.presentation.event.UiEvent
import com.grupo6.cruzdelsurapp.presentation.event.UiEventBus
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.text.Normalizer
import javax.inject.Inject

@HiltViewModel
class BuscarViewModel @Inject constructor(
    private val getEquipajesUseCase: GetEquipajesUseCase,
    private val eventBus: UiEventBus
): ViewModel() {
    private val _uiState = MutableStateFlow(BuscarUiState())
    val uiState = _uiState.asStateFlow()

    /** Se llama cada vez que se abre la pantalla, para consultar los datos actuales del servidor. */
    fun cargarEquipajes(){
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            getEquipajesUseCase()
                .onSuccess { equipajes ->
                    _uiState.update {
                        it.copy(
                            allEquipajes = equipajes,
                            // Se conserva lo que el usuario ya escribió
                            filteredEquipajes = filtrar(equipajes, it.query),
                            isLoading = false
                        )
                    }
                }
                .onFailure { error ->
                    _uiState.update { it.copy(isLoading = false) }
                    eventBus.sendEvent(UiEvent.ShowSnackbar(error.message ?: "Error al consultar el equipaje",
                        SnackbarType.ERROR))
                }
        }
    }

    fun onQueryChange(newQuery:String){
        _uiState.update { state ->
            state.copy(query = newQuery, filteredEquipajes = filtrar(state.allEquipajes, newQuery))
        }
    }

    /** Busca por pasajero, N.º de boleto o código de rastreo (CDS-...), sin importar mayúsculas, tildes ni espacios extra. */
    private fun filtrar(equipajes: List<Equipaje>, query: String): List<Equipaje> {
        val q = normalizar(query)
        if (q.isEmpty()) return equipajes
        return equipajes.filter { equipaje ->
            normalizar(equipaje.pasajero).contains(q) ||
                normalizar(equipaje.boleto).contains(q) ||
                normalizar(codigoRastreo(equipaje)).contains(q)
        }
    }

    private fun normalizar(texto: String): String =
        Normalizer.normalize(texto, Normalizer.Form.NFD)
            .replace(Regex("\\p{M}+"), "")
            .lowercase()
            .trim()
            .replace(Regex("\\s+"), " ")
}
