package com.grupo6.cruzdelsurapp.presentation.common

import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.grupo6.cruzdelsurapp.presentation.navigation.BottomNavItem
import com.grupo6.cruzdelsurapp.presentation.navigation.RutasNav
import com.grupo6.cruzdelsurapp.ui.theme.CdsNavy
import com.grupo6.cruzdelsurapp.ui.theme.CdsNavyContainer
import com.grupo6.cruzdelsurapp.ui.theme.CdsTextMuted

@Composable
fun AppBottomBar(navController: NavHostController){
    val opciones = listOf(BottomNavItem.Inicio, BottomNavItem.Form, BottomNavItem.Buscar)

    NavigationBar(containerColor = Color.White, tonalElevation = 0.dp) {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route

        opciones.forEach { item ->
            NavigationBarItem(
                icon = { Icon(item.icon, contentDescription = item.title) },
                label = { Text(item.title, fontWeight = FontWeight.SemiBold) },
                selected = currentRoute?.startsWith(item.route.split("?")[0]) == true,
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = CdsNavy,
                    selectedTextColor = CdsNavy,
                    indicatorColor = CdsNavyContainer,
                    unselectedIconColor = CdsTextMuted,
                    unselectedTextColor = CdsTextMuted
                ),
                onClick = {
                    navController.navigate(item.route) {
                        popUpTo(RutasNav.INICIO) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }
    }
}
