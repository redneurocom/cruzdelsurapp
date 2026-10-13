package com.grupo6.cruzdelsurapp.presentation.common

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.grupo6.cruzdelsurapp.presentation.event.UiEvent
import com.grupo6.cruzdelsurapp.presentation.event.UiEventBus
import com.grupo6.cruzdelsurapp.presentation.navigation.RutasNav
import com.grupo6.cruzdelsurapp.presentation.session.SesionManager
import com.grupo6.cruzdelsurapp.ui.theme.CdsBackground
import kotlinx.coroutines.flow.collectLatest

@Composable
fun AppScaffold(
    navController: NavHostController,
    eventBus: UiEventBus,
    sesionManager: SesionManager,
    content: @Composable (PaddingValues) -> Unit
){
    val snackbarHostState = remember { SnackbarHostState() }
    var snackbarType by remember { mutableStateOf(SnackbarType.SUCCESS) }
    val usuario by sesionManager.usuario.collectAsState()
    val ruta = navController.currentBackStackEntryAsState().value?.destination?.route
    val enLogin = ruta == null || ruta == RutasNav.LOGIN

    LaunchedEffect(Unit) {
        eventBus.events.collectLatest { event ->
            when (event) {
                is UiEvent.ShowSnackbar -> {
                    snackbarType = event.type
                    snackbarHostState.showSnackbar(event.message)
                }
            }
        }
    }

    Scaffold(
        snackbarHost = { CustomSnackbarHost(hostState = snackbarHostState, currentType = snackbarType) },
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
