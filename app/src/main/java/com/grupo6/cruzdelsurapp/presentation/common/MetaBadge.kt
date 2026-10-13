package com.grupo6.cruzdelsurapp.presentation.common

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun MetaBadge (text: String) {
    Surface(
        shape = RoundedCornerShape(10.dp),
        color = Color(0xFFEEF1F6)
    ) {
        Text(
            text = text,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF0E2340),
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
        )
    }
}