package com.grupo6.cruzdelsurapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.grupo6.cruzdelsurapp.presentation.common.AppScaffold
import com.grupo6.cruzdelsurapp.presentation.navigation.AppNavigation
import com.grupo6.cruzdelsurapp.presentation.session.SesionManager
import com.grupo6.cruzdelsurapp.ui.theme.CruzDelSurAppTheme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @Inject
    lateinit var sesionManager: SesionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CruzDelSurAppTheme{
                val navController = rememberNavController()
                AppScaffold(navController, sesionManager){paddingValues ->
                    Box(modifier = Modifier.fillMaxSize().padding(paddingValues)
                    ) {
                        AppNavigation(navController)
                    }
                }
            }
        }
    }
}
