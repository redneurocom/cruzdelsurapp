package com.grupo6.cruzdelsurapp

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.grupo6.cruzdelsurapp.domain.model.Equipaje
import com.grupo6.cruzdelsurapp.presentation.common.EquipajeItem
import com.grupo6.cruzdelsurapp.ui.theme.CruzDelSurAppTheme
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/** Pruebas instrumentadas (en el emulador) de la tarjeta del Manifiesto de Bodega. */
@RunWith(AndroidJUnit4::class)
class EquipajeItemTest {

    @get:Rule
    val composeRule = createComposeRule()

    private fun equipaje(pesoKg: Int) = Equipaje(
        id = "37c85b1c-de07-4bbe-9ef8-e4c2baea9e21", pasajero = "Ana Muerdas",
        boleto = "B001-0001", pesoKg = pesoKg, destino = "Puno", observaciones = "Frágil"
    )

    @Test
    fun identificadorDeLaApp() {
        val contexto = InstrumentationRegistry.getInstrumentation().targetContext
        assertEquals("appmovile.grupo6", contexto.packageName)
    }

    @Test
    fun muestraLosDatosDeLaUnidad() {
        composeRule.setContent { CruzDelSurAppTheme { EquipajeItem(equipaje(18), onDetalleClick = {}) } }
        composeRule.onNodeWithText("Ana Muerdas").assertIsDisplayed()
        composeRule.onNodeWithText("CDS-PUN-EA9E21").assertIsDisplayed()
        composeRule.onNodeWithText("B001-0001").assertIsDisplayed()
        composeRule.onNodeWithText("Cargado en bodega").assertIsDisplayed()
    }

    @Test
    fun avisaCuandoExcedeLaFranquicia() {
        composeRule.setContent { CruzDelSurAppTheme { EquipajeItem(equipaje(25), onDetalleClick = {}) } }
        composeRule.onNodeWithText("Exceso de peso").assertIsDisplayed()
    }

    @Test
    fun verDetalleEjecutaLaAccion() {
        var abierto = false
        composeRule.setContent { CruzDelSurAppTheme { EquipajeItem(equipaje(18), onDetalleClick = { abierto = true }) } }
        composeRule.onNodeWithText("Ver detalle").performClick()
        assertTrue(abierto)
    }
}
