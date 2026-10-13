package com.grupo6.cruzdelsurapp.domain.usecase

import com.grupo6.cruzdelsurapp.domain.model.Destino
import com.grupo6.cruzdelsurapp.domain.repository.EquipajeRepository
import javax.inject.Inject

class GetDestinosUseCase @Inject constructor(private val repository: EquipajeRepository) {
    suspend operator fun invoke(): List<Destino> = repository.getDestinos()
}