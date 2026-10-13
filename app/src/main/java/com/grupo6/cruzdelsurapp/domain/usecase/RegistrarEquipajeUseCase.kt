package com.grupo6.cruzdelsurapp.domain.usecase

import com.grupo6.cruzdelsurapp.domain.model.Equipaje
import com.grupo6.cruzdelsurapp.domain.repository.EquipajeRepository
import javax.inject.Inject

class RegistrarEquipajeUseCase @Inject constructor(private val repository: EquipajeRepository) {
    suspend operator fun invoke(equipaje: Equipaje): Result<Unit> = repository.registrarEquipaje(equipaje)
}