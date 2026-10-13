package com.jsalinas.cruzdelsurapp.domain.usecase

import com.jsalinas.cruzdelsurapp.domain.model.Destino
import com.jsalinas.cruzdelsurapp.domain.repository.EquipajeRepository
import javax.inject.Inject

class GetDestinosUseCase @Inject constructor(private val repository: EquipajeRepository) {
    suspend operator fun invoke(): Result<List<Destino>> = repository.getDestinos()
}
