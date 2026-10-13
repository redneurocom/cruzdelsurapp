package com.jsalinas.cruzdelsurapp.presentation.common

import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.jsalinas.cruzdelsurapp.presentation.navigation.BottomNavItem

@Composable
fun AppBottomBar(navController: NavHostController){
    val opciones = listOf(BottomNavItem.Inicio, BottomNavItem.Form, BottomNavItem.Buscar)

    NavigationBar {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route

        opciones.forEach { item ->
            NavigationBarItem(
                icon = { Icon(item.icon, contentDescription = item.title) },
                label = { Text(item.title) },
                selected = currentRoute?.startsWith(item.route.split("?")[0]) == true,
                onClick = {
                    navController.navigate(item.route) {
                        popUpTo(BottomNavItem.Inicio.route) {
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