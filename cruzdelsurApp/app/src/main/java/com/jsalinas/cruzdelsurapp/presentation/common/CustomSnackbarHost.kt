package com.jsalinas.cruzdelsurapp.presentation.common

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun CustomSnackbarHost(hostState: SnackbarHostState, currentType : SnackbarType) {
    SnackbarHost(hostState = hostState) { data ->
        val backgroundColor = when (currentType) {
            SnackbarType.SUCCESS -> Color(0xFF2E7D32)
            SnackbarType.ERROR -> Color(0xFFB71C1C)
            SnackbarType.WARNING -> Color(0xFFF57C00)
            SnackbarType.INFO -> Color(0xFF0288D1)
            SnackbarType.DEFAULT -> Color(0xFF000000)
        }

        Surface(
            modifier = Modifier.padding(16.dp),
            shape = RoundedCornerShape(8.dp),
            color = backgroundColor,
            contentColor = Color.White,
            shadowElevation = 6.dp
        ) {
            Text(
                text = data.visuals.message,
                modifier = Modifier.padding(16.dp),
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}