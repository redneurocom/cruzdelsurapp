package com.jsalinas.cruzdelsurapp.domain.usecase

import com.jsalinas.cruzdelsurapp.domain.repository.EquipajeRepository
import javax.inject.Inject

class EliminarEquipajeUseCase @Inject constructor(private val repository: EquipajeRepository) {
    suspend operator fun invoke(id: String): Result<Unit> = repository.eliminarEquipaje(id)
}
