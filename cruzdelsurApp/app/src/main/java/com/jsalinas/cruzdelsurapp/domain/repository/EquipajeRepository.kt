package com.jsalinas.cruzdelsurapp.domain.repository

import com.jsalinas.cruzdelsurapp.domain.model.Destino
import com.jsalinas.cruzdelsurapp.domain.model.Equipaje

interface EquipajeRepository {
    suspend fun getEquipajes(): Result<List<Equipaje>>
    suspend fun getEquipajeById(id: String): Result<Equipaje>
    suspend fun insertarEquipaje(equipaje: Equipaje): Result<Unit>
    suspend fun actualizarEquipaje(id: String, equipaje: Equipaje): Result<Unit>
    suspend fun eliminarEquipaje(id: String): Result<Unit>
    suspend fun getDestinos(): Result<List<Destino>>
}
