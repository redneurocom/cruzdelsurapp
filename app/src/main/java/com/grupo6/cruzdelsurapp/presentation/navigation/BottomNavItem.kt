package com.grupo6.cruzdelsurapp.presentation.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Inventory2
import androidx.compose.material.icons.filled.Luggage
import androidx.compose.material.icons.filled.TravelExplore
import androidx.compose.ui.graphics.vector.ImageVector

sealed class BottomNavItem(val route: String, val title: String, val icon: ImageVector) {
    object Inicio : BottomNavItem(RutasNav.INICIO, "Manifiesto", Icons.Default.Inventory2)
    object Form : BottomNavItem(RutasNav.FORMULARIO, "Registrar", Icons.Default.Luggage) {
        fun passId(id: String? = null): String = if (id != null) "form?id=$id" else "form"
    }
    object Buscar : BottomNavItem(RutasNav.BUSCAR, "Consultar", Icons.Default.TravelExplore)
    object Detalle : BottomNavItem(RutasNav.DETALLE, "Detalle", Icons.Default.Luggage) {
        fun passId(id: String): String = "detalle/$id"
    }
}
