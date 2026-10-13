package com.jsalinas.cruzdelsurapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.jsalinas.cruzdelsurapp.presentation.common.AppScaffold
import com.jsalinas.cruzdelsurapp.presentation.event.UiEventBus
import com.jsalinas.cruzdelsurapp.presentation.navigation.AppNavigation
import com.jsalinas.cruzdelsurapp.ui.theme.CruzDelSurTheme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @Inject
    lateinit var eventBus : UiEventBus

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CruzDelSurTheme{
                val navController = rememberNavController()
                AppScaffold(navController, eventBus){paddingValues ->
                    Box(modifier = Modifier.fillMaxSize().padding(paddingValues)
                    ) {
                        AppNavigation(navController)
                    }
                }
            }
        }
    }
}
