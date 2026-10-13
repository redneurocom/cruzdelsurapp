package com.grupo6.cruzdelsurapp.presentation.screens.buscar

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
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
import androidx.compose.material.icons.automirrored.outlined.HelpOutline
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.ConfirmationNumber
import androidx.compose.material.icons.filled.SearchOff
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.TravelExplore
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.grupo6.cruzdelsurapp.R
import com.grupo6.cruzdelsurapp.presentation.common.CustomSnackbarHost
import com.grupo6.cruzdelsurapp.presentation.common.EquipajeItem
import com.grupo6.cruzdelsurapp.presentation.common.LoadingOverlay
import com.grupo6.cruzdelsurapp.presentation.common.cdsFieldColors
import com.grupo6.cruzdelsurapp.ui.theme.CdsBorder
import com.grupo6.cruzdelsurapp.ui.theme.CdsField
import com.grupo6.cruzdelsurapp.ui.theme.CdsGreen
import com.grupo6.cruzdelsurapp.ui.theme.CdsGreenContainer
import com.grupo6.cruzdelsurapp.ui.theme.CdsNavy
import com.grupo6.cruzdelsurapp.ui.theme.CdsNavyContainer
import com.grupo6.cruzdelsurapp.ui.theme.CdsNavyLight
import com.grupo6.cruzdelsurapp.ui.theme.CdsText
import com.grupo6.cruzdelsurapp.ui.theme.CdsTextMuted

@Composable
fun BuscarScreen(
    onDetalleEquipaje: (String) -> Unit,
    viewModel: BuscarViewModel = hiltViewModel()
) {
    val uiState = viewModel.uiState
    val snackbarHostState = remember { SnackbarHostState() }

    // Consulta los datos actuales cada vez que se abre la pantalla
    LaunchedEffect(Unit) {
        viewModel.cargarEquipajes()
    }

    LaunchedEffect(uiState.mensaje) {
        uiState.mensaje?.let {
            snackbarHostState.showSnackbar(it)
            viewModel.onMensajeMostrado()
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item(key = "banner") { BannerCustodia() }

            item(key = "titulo") {
                Column(modifier = Modifier.padding(top = 4.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.TravelExplore, contentDescription = null, tint = CdsNavy, modifier = Modifier.size(26.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "¿Dónde está mi equipaje?",
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Bold,
                            color = CdsText
                        )
                    }
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "Consulta el estado de tus maletas en bodega ingresando el nombre del pasajero titular, el número de boleto o el código de rastreo de tu marbete.",
                        fontSize = 14.sp,
                        color = CdsTextMuted,
                        lineHeight = 20.sp
                    )
                }
            }

            item(key = "buscador") {
                Card(
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    border = BorderStroke(1.dp, CdsBorder),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "Pasajero, N.º de boleto o código CDS",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = CdsText
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        OutlinedTextField(
                            value = uiState.query,
                            onValueChange = viewModel::onQueryChange,
                            modifier = Modifier.fillMaxWidth(),
                            placeholder = { Text("Ej. Elena Morales, B001-0029412 o CDS-ARE-…") },
                            leadingIcon = { Icon(Icons.Default.ConfirmationNumber, contentDescription = null) },
                            trailingIcon = {
                                if (uiState.query.isNotEmpty()) {
                                    IconButton(onClick = { viewModel.onQueryChange("") }) {
                                        Icon(Icons.Default.Close, contentDescription = "Limpiar")
                                    }
                                }
                            },
                            singleLine = true,
                            shape = RoundedCornerShape(14.dp),
                            colors = cdsFieldColors()
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "La búsqueda se actualiza mientras escribes.",
                            fontSize = 11.sp,
                            color = CdsTextMuted
                        )
                    }
                }
            }

            if (uiState.query.isBlank()) {
                item(key = "ayuda") { AyudaCodigo() }
            } else {
                item(key = "resultados") {
                    Text(
                        text = "Resultados (${uiState.filteredEquipajes.size})",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = CdsText,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }
                if (uiState.filteredEquipajes.isEmpty() && !uiState.isLoading) {
                    item(key = "sin_resultados") { SinResultados() }
                }
                items(uiState.filteredEquipajes, key = { it.id }) { equipaje ->
                    EquipajeItem(
                        equipaje = equipaje,
                        onDetalleClick = {onDetalleEquipaje(equipaje.id)}
                    )
                }
            }
        }

        LoadingOverlay(isLoading = uiState.isLoading)

        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.BottomCenter) {
            CustomSnackbarHost(hostState = snackbarHostState, currentType = uiState.tipoMensaje)
        }
    }
}

@Composable
private fun BannerCustodia() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(170.dp)
            .clip(RoundedCornerShape(22.dp))
            .background(Brush.linearGradient(listOf(CdsNavyLight, CdsNavy)))
    ) {
        Image(
            painter = painterResource(id = R.drawable.ilustracion_bus),
            contentDescription = null,
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(top = 12.dp, end = 8.dp)
                .width(200.dp)
                .height(110.dp)
        )
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Brush.verticalGradient(listOf(Color.Transparent, CdsNavy.copy(alpha = 0.9f))))
        )
        Column(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(16.dp)
        ) {
            Surface(shape = CircleShape, color = Color.White.copy(alpha = 0.18f)) {
                Row(
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.Default.Shield, contentDescription = null, tint = Color.White, modifier = Modifier.size(13.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("CUSTODIA GARANTIZADA", color = Color.White, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Viaja tranquilo, nosotros cuidamos tus maletas",
                color = Color.White,
                fontSize = 19.sp,
                fontWeight = FontWeight.Bold,
                lineHeight = 24.sp
            )
        }
    }
}

@Composable
private fun AyudaCodigo() {
    Card(
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = CdsField),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.AutoMirrored.Outlined.HelpOutline, contentDescription = null, tint = CdsNavy, modifier = Modifier.size(20.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text("¿Dónde encuentro estos datos?", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = CdsText)
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Al entregar tu equipaje en el counter recibirás un talón como este. Usa el nombre del pasajero o el número de boleto impreso.",
                fontSize = 13.sp,
                color = CdsTextMuted,
                lineHeight = 19.sp
            )
            Spacer(modifier = Modifier.height(12.dp))

            // Talón de ejemplo
            Surface(shape = RoundedCornerShape(16.dp), color = Color.White, modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text("TALÓN DE EQUIPAJE", fontSize = 10.sp, color = CdsTextMuted, fontWeight = FontWeight.SemiBold)
                            Text("EJEMPLO", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = CdsText)
                        }
                        Text(
                            "Bodega A1",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = CdsGreen,
                            modifier = Modifier
                                .background(CdsGreenContainer, RoundedCornerShape(6.dp))
                                .padding(horizontal = 8.dp, vertical = 3.dp)
                        )
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                    HorizontalDivider(color = CdsBorder)
                    Spacer(modifier = Modifier.height(10.dp))
                    FilaTalon("Pasajero", "JUAN P. QUISPE")
                    FilaTalon("Ruta", "LIMA → AREQUIPA")
                    Spacer(modifier = Modifier.height(8.dp))
                    Surface(shape = RoundedCornerShape(12.dp), color = CdsNavyContainer, modifier = Modifier.fillMaxWidth()) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Text("N.º DE BOLETO", fontSize = 10.sp, color = CdsNavy, fontWeight = FontWeight.SemiBold)
                            Text(
                                "B001-0029412",
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold,
                                fontFamily = FontFamily.Monospace,
                                color = CdsNavy
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun FilaTalon(etiqueta: String, valor: String) {
    Row(modifier = Modifier.fillMaxWidth().padding(vertical = 2.dp)) {
        Text("$etiqueta:", fontSize = 12.sp, color = CdsTextMuted, modifier = Modifier.weight(1f))
        Text(valor, fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = CdsText)
    }
}

@Composable
private fun SinResultados() {
    Column(
        modifier = Modifier.fillMaxWidth().padding(vertical = 30.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(Icons.Default.SearchOff, contentDescription = null, tint = CdsTextMuted, modifier = Modifier.size(44.dp))
        Spacer(modifier = Modifier.height(8.dp))
        Text("No encontramos equipaje", fontWeight = FontWeight.Bold, color = CdsText)
        Text(
            "Revisa el nombre o el número de boleto e inténtalo nuevamente.",
            fontSize = 13.sp,
            color = CdsTextMuted,
            textAlign = TextAlign.Center
        )
    }
}
