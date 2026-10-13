package com.grupo6.cruzdelsurapp.domain.repository

import com.grupo6.cruzdelsurapp.domain.model.Destino
import com.grupo6.cruzdelsurapp.domain.model.Equipaje

interface EquipajeRepository {
    suspend fun getEquipajes(): List<Equipaje>
    suspend fun getEquipajeById(id: String): Equipaje
    suspend fun registrarEquipaje(equipaje: Equipaje)
    suspend fun actualizarEquipaje(id: String, equipaje: Equipaje)
    suspend fun eliminarEquipaje(id: String)
    suspend fun getDestinos(): List<Destino>
}