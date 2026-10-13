package com.grupo6.cruzdelsurapp.presentation.screens.inicio

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DirectionsBus
import androidx.compose.material.icons.filled.Inventory2
import androidx.compose.material.icons.filled.Luggage
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.grupo6.cruzdelsurapp.presentation.common.CuadroInfo
import com.grupo6.cruzdelsurapp.presentation.common.EquipajeItem
import com.grupo6.cruzdelsurapp.presentation.common.LoadingScreen
import com.grupo6.cruzdelsurapp.ui.theme.CdsField
import com.grupo6.cruzdelsurapp.ui.theme.CdsNavy
import com.grupo6.cruzdelsurapp.ui.theme.CdsNavyLight
import com.grupo6.cruzdelsurapp.ui.theme.CdsText
import com.grupo6.cruzdelsurapp.ui.theme.CdsTextMuted

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
        LoadingScreen("Cargando manifiesto de bodega...")
    }else{
        // Agrupación visual por destino (solo presentación)
        val porDestino = uiState.equipajes.groupBy { it.destino.ifBlank { "Sin destino" } }
        val pesoTotal = uiState.equipajes.sumOf { it.pesoKg }

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item(key = "resumen") {
                ResumenBodega(
                    total = uiState.equipajes.size,
                    pesoTotal = pesoTotal,
                    destinos = porDestino.size
                )
            }

            item(key = "total") {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Unidades a bordo",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = CdsText
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    CuadroInfo(Icons.Default.Luggage, "Total: ${uiState.equipajes.size} bultos")
                }
            }

            if (uiState.equipajes.isEmpty()) {
                item(key = "vacio") { ManifiestoVacio() }
            }

            porDestino.forEach { (destino, unidades) ->
                item(key = "destino_$destino") {
                    EncabezadoDestino(destino = destino, cantidad = unidades.size)
                }
                items(unidades, key = { it.id }) { equipaje ->
                    EquipajeItem(
                        equipaje = equipaje,
                        onDetalleClick = {onDetalleEquipaje(equipaje.id)}
                    )
                }
            }
        }
    }
}

@Composable
private fun ResumenBodega(total: Int, pesoTotal: Int, destinos: Int) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(22.dp))
            .background(Brush.linearGradient(listOf(CdsNavy, CdsNavyLight)))
            .padding(18.dp)
    ) {
        Column {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Surface(shape = CircleShape, color = Color.White.copy(alpha = 0.12f)) {
                    Row(
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(modifier = Modifier.size(7.dp).background(Color(0xFF4ADE80), CircleShape))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "Bodega en operación",
                            color = Color.White,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
                Spacer(modifier = Modifier.weight(1f))
                Surface(shape = CircleShape, color = Color.White.copy(alpha = 0.12f), modifier = Modifier.size(40.dp)) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(Icons.Default.DirectionsBus, contentDescription = null, tint = Color.White)
                    }
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Control de equipaje en bodega",
                color = Color.White,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "Unidades registradas agrupadas por destino",
                color = Color.White.copy(alpha = 0.7f),
                fontSize = 13.sp
            )
            Spacer(modifier = Modifier.height(16.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                Estadistica("Total", "$total", "bultos", Modifier.weight(1f))
                Estadistica("Peso", "$pesoTotal", "kg en bodega", Modifier.weight(1f), destacado = true)
                Estadistica("Destinos", "$destinos", "rutas", Modifier.weight(1f))
            }
        }
    }
}

@Composable
private fun Estadistica(
    etiqueta: String,
    valor: String,
    detalle: String,
    modifier: Modifier = Modifier,
    destacado: Boolean = false
) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(14.dp),
        color = Color.White.copy(alpha = 0.09f)
    ) {
        Column(
            modifier = Modifier.padding(vertical = 10.dp, horizontal = 6.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(etiqueta, color = Color.White.copy(alpha = 0.7f), fontSize = 11.sp)
            Text(
                valor,
                color = if (destacado) Color(0xFF4ADE80) else Color.White,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                detalle,
                color = Color.White.copy(alpha = 0.7f),
                fontSize = 11.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Composable
private fun EncabezadoDestino(destino: String, cantidad: Int) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(top = 6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(modifier = Modifier.size(8.dp).background(CdsNavy, CircleShape))
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = "Destino: $destino",
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = CdsText,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.weight(1f)
        )
        Text(
            text = "$cantidad ${if (cantidad == 1) "bulto" else "bultos"}",
            fontSize = 12.sp,
            fontWeight = FontWeight.SemiBold,
            color = CdsTextMuted,
            modifier = Modifier
                .background(CdsField, RoundedCornerShape(50))
                .padding(horizontal = 10.dp, vertical = 3.dp)
        )
    }
}

@Composable
private fun ManifiestoVacio() {
    Column(
        modifier = Modifier.fillMaxWidth().padding(vertical = 40.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(Icons.Default.Inventory2, contentDescription = null, tint = CdsTextMuted, modifier = Modifier.size(48.dp))
        Spacer(modifier = Modifier.height(10.dp))
        Text("La bodega está vacía", fontWeight = FontWeight.Bold, color = CdsText)
        Text(
            "Registra una unidad desde la pestaña Registrar.",
            color = CdsTextMuted,
            fontSize = 13.sp,
            textAlign = TextAlign.Center
        )
    }
}
