package com.grupo6.cruzdelsurapp.presentation.common

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.grupo6.cruzdelsurapp.presentation.navigation.RutasNav
import com.grupo6.cruzdelsurapp.presentation.session.SesionManager
import com.grupo6.cruzdelsurapp.ui.theme.CdsBackground

@Composable
fun AppScaffold(
    navController: NavHostController,
    sesionManager: SesionManager,
    content: @Composable (PaddingValues) -> Unit
){
    val usuario = sesionManager.usuario
    val ruta = navController.currentBackStackEntryAsState().value?.destination?.route
    val enLogin = ruta == null || ruta == RutasNav.LOGIN || ruta == RutasNav.RECUPERAR

    Scaffold(
        topBar = {
            if (!enLogin) {
                AppTopBar(
                    navController = navController,
                    usuario = usuario,
                    onCerrarSesion = {
                        sesionManager.cerrarSesion()
                        navController.navigate(RutasNav.LOGIN) {
                            popUpTo(navController.graph.id) { inclusive = true }
                            launchSingleTop = true
                        }
                    }
                )
            }
        },
        // Las pestañas de operario solo se muestran con sesión iniciada
        bottomBar = { if (!enLogin && usuario != null) AppBottomBar(navController) },
        containerColor = CdsBackground
    ) {paddingValues ->
        content(paddingValues)
    }
}
