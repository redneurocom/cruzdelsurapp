package com.grupo6.cruzdelsurapp.domain.repository

import com.grupo6.cruzdelsurapp.domain.model.Destino
import com.grupo6.cruzdelsurapp.domain.model.Equipaje

interface EquipajeRepository {
    suspend fun getEquipajes(): Result<List<Equipaje>>
    suspend fun getEquipajeById(id: String): Result<Equipaje>
    suspend fun registrarEquipaje(equipaje: Equipaje): Result<Unit>
    suspend fun actualizarEquipaje(id: String, equipaje: Equipaje): Result<Unit>
    suspend fun eliminarEquipaje(id: String): Result<Unit>
    suspend fun getDestinos(): Result<List<Destino>>
}