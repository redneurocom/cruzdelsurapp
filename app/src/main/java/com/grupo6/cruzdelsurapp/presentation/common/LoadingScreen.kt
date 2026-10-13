package com.grupo6.cruzdelsurapp.presentation.common

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.grupo6.cruzdelsurapp.ui.theme.CdsNavy
import com.grupo6.cruzdelsurapp.ui.theme.CdsTextMuted

@Composable
fun LoadingScreen(mensaje: String = "Cargando..."){
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        LogoCds(size = 56.dp)
        Spacer(modifier = Modifier.height(20.dp))
        CircularProgressIndicator(color = CdsNavy)
        Spacer(modifier = Modifier.height(12.dp))
        Text(
            text = mensaje,
            style = MaterialTheme.typography.bodyMedium,
            color = CdsTextMuted
        )
    }
}
