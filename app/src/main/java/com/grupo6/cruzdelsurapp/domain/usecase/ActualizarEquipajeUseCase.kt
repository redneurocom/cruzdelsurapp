package com.grupo6.cruzdelsurapp.domain.usecase

import com.grupo6.cruzdelsurapp.domain.model.Equipaje
import com.grupo6.cruzdelsurapp.domain.repository.EquipajeRepository
import javax.inject.Inject

class ActualizarEquipajeUseCase @Inject constructor(private val repository: EquipajeRepository) {
    suspend operator fun invoke(id: String, equipaje: Equipaje): Result<Unit> = repository.actualizarEquipaje(id, equipaje)
}