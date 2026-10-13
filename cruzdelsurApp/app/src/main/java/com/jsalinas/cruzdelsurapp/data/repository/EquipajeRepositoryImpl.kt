package com.jsalinas.cruzdelsurapp.data.repository

import com.jsalinas.cruzdelsurapp.data.remote.api.DestinoApiService
import com.jsalinas.cruzdelsurapp.data.remote.api.EquipajeApiService
import com.jsalinas.cruzdelsurapp.data.remote.dto.InsertarEquipajeDto
import com.jsalinas.cruzdelsurapp.domain.model.Destino
import com.jsalinas.cruzdelsurapp.domain.model.Equipaje
import com.jsalinas.cruzdelsurapp.domain.repository.EquipajeRepository
import javax.inject.Inject

class EquipajeRepositoryImpl @Inject constructor(
    private val apiEquipaje: EquipajeApiService,
    private val apiDestino: DestinoApiService
) : EquipajeRepository{
    override suspend fun getEquipajes(): Result<List<Equipaje>> = runCatching {
        val response = apiEquipaje.getEquipajes()
        if(response.success){
            response.data.items.map { it.toDomain() }
        }else{
            throw Exception("Error en la respuesta del servidor")
        }
    }

    override suspend fun getEquipajeById(id: String): Result<Equipaje> = runCatching {
        val response = apiEquipaje.getEquipajeById(id)
        if(response.success && response.data != null){
            response.data.toDomain()
        }else{
            throw Exception("No se pudo obtener el detalle del equipaje")
        }
    }

    override suspend fun insertarEquipaje(equipaje: Equipaje): Result<Unit> = runCatching {
        val dto = InsertarEquipajeDto(
            boleto = equipaje.boleto,
            pasajero = equipaje.pasajero,
            destino = equipaje.destino,
            tipo = equipaje.tipo,
            peso = equipaje.peso,
            observaciones = equipaje.observaciones
        )
        apiEquipaje.insertarEquipaje(dto)
    }

    override suspend fun actualizarEquipaje(id: String, equipaje: Equipaje): Result<Unit> = runCatching {
        val dto = InsertarEquipajeDto(
            boleto = equipaje.boleto,
            pasajero = equipaje.pasajero,
            destino = equipaje.destino,
            tipo = equipaje.tipo,
            peso = equipaje.peso,
            observaciones = equipaje.observaciones
        )
        apiEquipaje.actualizarEquipaje(id, dto)
    }

    override suspend fun eliminarEquipaje(id: String): Result<Unit> = runCatching {
        val response = apiEquipaje.eliminarEquipaje(id)
        if(!response.isSuccessful){
            throw Exception("Error en la respuesta del servidor")
        }
    }

    override suspend fun getDestinos(): Result<List<Destino>> = runCatching {
        val response = apiDestino.getDestinos()
        if (response.success) {
            response.data.items.map { it.toDomain() }
        } else {
            throw Exception("Error al cargar los destinos")
        }
    }
}
