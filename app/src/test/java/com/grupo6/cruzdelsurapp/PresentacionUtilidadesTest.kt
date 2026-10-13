package com.grupo6.cruzdelsurapp

import com.grupo6.cruzdelsurapp.domain.model.Equipaje
import com.grupo6.cruzdelsurapp.presentation.common.FRANQUICIA_KG
import com.grupo6.cruzdelsurapp.presentation.common.codigoRastreo
import com.grupo6.cruzdelsurapp.presentation.common.iniciales
import com.grupo6.cruzdelsurapp.presentation.common.nombreRol
import org.junit.Assert.assertEquals
import org.junit.Test

/** Pruebas de las funciones de presentación propias de la app (se ejecutan en la JVM). */
class PresentacionUtilidadesTest {

    private fun equipaje(id: String, destino: String) = Equipaje(
        id = id, pasajero = "Ana Muerdas", boleto = "B001-0001",
        pesoKg = 20, destino = destino, observaciones = "Frágil"
    )

    @Test
    fun codigoRastreo_usaDestinoYUltimosSeisDelId() {
        // Mismo formato que guarda funEquipajes en DynamoDB
        assertEquals("CDS-PUN-EA9E21", codigoRastreo(equipaje("37c85b1c-de07-4bbe-9ef8-e4c2baea9e21", "Puno")))
        assertEquals("CDS-ARE-A04821", codigoRastreo(equipaje("0b1f6c1e-3c1a-4f7e-9a51-1d2c00a04821", "Arequipa")))
    }

    @Test
    fun codigoRastreo_sinDatos_usaValoresPorDefecto() {
        assertEquals("CDS-GEN-000000", codigoRastreo(equipaje("", "")))
    }

    @Test
    fun iniciales_tomaLasDosPrimerasPalabras() {
        assertEquals("CM", iniciales("Carlos Mendoza"))
        assertEquals("EM", iniciales("  elena   morales vargas "))
        assertEquals("?", iniciales(""))
    }

    @Test
    fun nombreRol_traduceLosRolesDeLaTablaUsuarios() {
        assertEquals("Operario de bodega", nombreRol("OPERARIO_BODEGA"))
        assertEquals("Operario de counter", nombreRol("OPERARIO_COUNTER"))
        assertEquals("Supervisor", nombreRol("SUPERVISOR"))
    }

    @Test
    fun franquicia_esDe20Kg() {
        assertEquals(20, FRANQUICIA_KG)
    }
}
