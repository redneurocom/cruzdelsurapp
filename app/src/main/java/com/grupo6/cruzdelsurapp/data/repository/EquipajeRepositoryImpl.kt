package com.grupo6.cruzdelsurapp.data.repository

import com.grupo6.cruzdelsurapp.data.remote.api.DestinoApiService
import com.grupo6.cruzdelsurapp.data.remote.api.EquipajeApiService
import com.grupo6.cruzdelsurapp.data.remote.dto.RegistrarEquipajeDto
import com.grupo6.cruzdelsurapp.domain.model.Destino
import com.grupo6.cruzdelsurapp.domain.model.Equipaje
import com.grupo6.cruzdelsurapp.domain.repository.EquipajeRepository
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

    override suspend fun registrarEquipaje(equipaje: Equipaje): Result<Unit> = runCatching {
        val dto = RegistrarEquipajeDto(
            pasajero = equipaje.pasajero,
            boleto = equipaje.boleto,
            pesoKg = equipaje.pesoKg,
            destino = equipaje.destino,
            observaciones = equipaje.observaciones
        )
        apiEquipaje.registrarEquipaje(dto)
    }

    override suspend fun actualizarEquipaje(id: String, equipaje: Equipaje): Result<Unit> = runCatching {
        val dto = RegistrarEquipajeDto(
            pasajero = equipaje.pasajero,
            boleto = equipaje.boleto,
            pesoKg = equipaje.pesoKg,
            destino = equipaje.destino,
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