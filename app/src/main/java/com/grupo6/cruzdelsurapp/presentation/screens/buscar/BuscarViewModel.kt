package com.grupo6.cruzdelsurapp.presentation.screens.buscar

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.grupo6.cruzdelsurapp.domain.model.Equipaje
import com.grupo6.cruzdelsurapp.domain.usecase.GetEquipajesUseCase
import com.grupo6.cruzdelsurapp.presentation.common.SnackbarType
import com.grupo6.cruzdelsurapp.presentation.common.codigoRastreo
import com.grupo6.cruzdelsurapp.presentation.session.SesionManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.text.Normalizer
import javax.inject.Inject

@HiltViewModel
class BuscarViewModel @Inject constructor(
    private val getEquipajesUseCase: GetEquipajesUseCase,
    sesionManager: SesionManager
): ViewModel() {
    // Reutiliza la misma pantalla para el pasajero (sin sesión) y el operario (con sesión);
    // solo cambia el texto para que cada uno la lea en su propio contexto.
    var uiState by mutableStateOf(BuscarUiState(esOperario = sesionManager.usuario != null))
        private set

    fun onMensajeMostrado() { uiState = uiState.copy(mensaje = null) }

    // Se llama cada vez que se abre la pantalla, para consultar los datos actuales del servidor
    fun cargarEquipajes(){
        viewModelScope.launch {
            uiState = uiState.copy(isLoading = true)
            try {
                val equipajes = getEquipajesUseCase()
                uiState = uiState.copy(
                    allEquipajes = equipajes,
                    filteredEquipajes = filtrar(equipajes, uiState.query),
                    isLoading = false
                )
            } catch (e: Exception) {
                uiState = uiState.copy(
                    isLoading = false,
                    mensaje = e.message ?: "Error al consultar el equipaje",
                    tipoMensaje = SnackbarType.ERROR
                )
            }
        }
    }

    fun onQueryChange(newQuery:String){
        uiState = uiState.copy(query = newQuery, filteredEquipajes = filtrar(uiState.allEquipajes, newQuery))
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
