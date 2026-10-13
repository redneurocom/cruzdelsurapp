package com.jsalinas.cruzdelsurapp.presentation.common

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.jsalinas.cruzdelsurapp.presentation.event.UiEvent
import com.jsalinas.cruzdelsurapp.presentation.event.UiEventBus
import com.jsalinas.cruzdelsurapp.presentation.navigation.RutasNav
import kotlinx.coroutines.flow.collectLatest

@Composable
fun AppScaffold(navController: NavHostController, eventBus : UiEventBus, content: @Composable (PaddingValues) -> Unit){

    val snackbarHostState = remember { SnackbarHostState() }
    var snackbarType by remember { mutableStateOf(SnackbarType.SUCCESS) }
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val ruta = navBackStackEntry?.destination?.route
    val mostrarBarras = ruta != RutasNav.LOGIN && ruta != RutasNav.RECUPERAR

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
        topBar = { if (mostrarBarras) AppTopBar(navController) },
        bottomBar = { if (mostrarBarras) AppBottomBar(navController) }
    ) {paddingValues ->
        content(paddingValues)
    }
}