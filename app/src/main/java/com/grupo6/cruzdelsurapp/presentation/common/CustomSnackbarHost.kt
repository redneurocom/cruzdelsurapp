package com.grupo6.cruzdelsurapp.presentation.common

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
import com.grupo6.cruzdelsurapp.ui.theme.CdsAlert
import com.grupo6.cruzdelsurapp.ui.theme.CdsGreen
import com.grupo6.cruzdelsurapp.ui.theme.CdsNavy
import com.grupo6.cruzdelsurapp.ui.theme.CdsNavyLight
import com.grupo6.cruzdelsurapp.ui.theme.CdsWarning

@Composable
fun CustomSnackbarHost(hostState: SnackbarHostState, currentType : SnackbarType) {
    SnackbarHost(hostState = hostState) { data ->
        val backgroundColor = when (currentType) {
            SnackbarType.SUCCESS -> CdsGreen
            SnackbarType.ERROR -> CdsAlert
            SnackbarType.WARNING -> CdsWarning
            SnackbarType.INFO -> CdsNavyLight
            SnackbarType.DEFAULT -> CdsNavy
        }

        Surface(
            modifier = Modifier.padding(16.dp),
            shape = RoundedCornerShape(14.dp),
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