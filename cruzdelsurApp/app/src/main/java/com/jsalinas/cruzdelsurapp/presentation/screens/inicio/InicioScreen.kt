package com.jsalinas.cruzdelsurapp.presentation.screens.inicio

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Luggage
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.jsalinas.cruzdelsurapp.presentation.common.CuadroInfo
import com.jsalinas.cruzdelsurapp.presentation.common.EquipajeItem
import com.jsalinas.cruzdelsurapp.presentation.common.LoadingScreen

@Composable
fun InicioScreen(
    onDetalleEquipaje: (String) -> Unit,
    viewModel: InicioViewModel = hiltViewModel()
){
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.cargarEquipajes()
    }

    if(uiState.isLoading){
        LoadingScreen()
    }else{
        Column(modifier = Modifier.fillMaxSize().padding(top = 16.dp, bottom = 10.dp)){
            // Saludo del operario
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFF2F4F7)),
                modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(text = "¡Buen día!", fontSize = 13.sp, color = Color(0xFF555B6E))
                    Text(
                        text = "Operario de counter",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF0B2545)
                    )
                    Spacer(modifier = Modifier.padding(4.dp))
                    CuadroInfo(Icons.Outlined.LocationOn, "Terminal Javier Prado • Lima")
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth().padding(top = 16.dp, start = 16.dp, end = 10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Equipajes registrados",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF0B2545)
                )

                Spacer(modifier = Modifier.weight(1f)) // Empuja todo lo que esté después a la derecha

                CuadroInfo(Icons.Default.Luggage,"Total: ${uiState.equipajes.size}")
            }

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(uiState.equipajes, key = { it.id }) { equipaje ->
                    EquipajeItem(
                        equipaje = equipaje,
                        onDetalleClick = {onDetalleEquipaje(equipaje.id)}
                    )
                }
            }
        }
    }
}
