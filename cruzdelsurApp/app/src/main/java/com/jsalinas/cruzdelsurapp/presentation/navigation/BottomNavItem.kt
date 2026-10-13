package com.jsalinas.cruzdelsurapp.presentation.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.ui.graphics.vector.ImageVector

sealed class BottomNavItem(val route: String, val title: String, val icon: ImageVector) {
    object Inicio : BottomNavItem(RutasNav.INICIO, "Inicio", Icons.Default.Home)
    object Form : BottomNavItem(RutasNav.FORMULARIO, "Registrar", Icons.Default.Add) {
        fun passId(id: String? = null): String = if (id != null) "form?id=$id" else "form"
    }
    object Buscar : BottomNavItem(RutasNav.BUSCAR, "Buscar", Icons.Default.Search)
    object Detalle : BottomNavItem(RutasNav.DETALLE, "Detalle", Icons.Default.Home) {
        fun passId(id: String): String = "detalle/$id"
    }
}
