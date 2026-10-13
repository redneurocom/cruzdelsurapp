package com.grupo6.cruzdelsurapp.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.grupo6.cruzdelsurapp.presentation.screens.buscar.BuscarScreen
import com.grupo6.cruzdelsurapp.presentation.screens.detalle.DetalleScreen
import com.grupo6.cruzdelsurapp.presentation.screens.form.FormScreen
import com.grupo6.cruzdelsurapp.presentation.screens.inicio.InicioScreen
import com.grupo6.cruzdelsurapp.presentation.screens.login.LoginScreen
import com.grupo6.cruzdelsurapp.presentation.screens.recuperar.RecuperarScreen

@Composable
fun AppNavigation(navController: NavHostController){
    NavHost(
        navController = navController,
        startDestination = RutasNav.LOGIN,
        modifier = Modifier
    ){
        composable(RutasNav.LOGIN){
            LoginScreen(
                onLoginExitoso = {
                    navController.navigate(BottomNavItem.Inicio.route) {
                        popUpTo(RutasNav.LOGIN) { inclusive = true }
                    }
                },
                onConsultarEquipaje = {
                    navController.navigate(BottomNavItem.Buscar.route)
                },
                onRecuperarPassword = {
                    navController.navigate(RutasNav.RECUPERAR)
                }
            )
        }

        composable(RutasNav.RECUPERAR){
            RecuperarScreen(onVolver = { navController.popBackStack() })
        }

        composable(BottomNavItem.Inicio.route){
            InicioScreen(
                onDetalleEquipaje = { id ->
                    navController.navigate(BottomNavItem.Detalle.passId(id))
                }
            )
        }

        composable(
            route = BottomNavItem.Detalle.route,
            arguments = listOf(navArgument("id") {type = NavType.StringType})
        ){
            DetalleScreen(
                onEditClick = { id ->
                    navController.navigate(BottomNavItem.Form.passId(id))
                },
                onEquipajeDeleted = {
                    navController.popBackStack()
                }
            )
        }

        composable(
            route = BottomNavItem.Form.route,
            arguments = listOf(navArgument("id") {
                type = NavType.StringType
                nullable = true
                defaultValue = null
            })
        ){
            FormScreen(onSuccess = {navController.popBackStack()})
        }

        composable(BottomNavItem.Buscar.route){
            BuscarScreen(
                onDetalleEquipaje = {id->
                    navController.navigate(BottomNavItem.Detalle.passId(id))
                }
            )
        }
    }
}