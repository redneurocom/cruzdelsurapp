package com.jsalinas.cruzdelsurapp.domain.usecase

import com.jsalinas.cruzdelsurapp.domain.model.Equipaje
import com.jsalinas.cruzdelsurapp.domain.repository.EquipajeRepository
import javax.inject.Inject

class ActualizarEquipajeUseCase @Inject constructor(private val repository: EquipajeRepository) {
    suspend operator fun invoke(id: String, equipaje: Equipaje): Result<Unit> = repository.actualizarEquipaje(id, equipaje)
}
