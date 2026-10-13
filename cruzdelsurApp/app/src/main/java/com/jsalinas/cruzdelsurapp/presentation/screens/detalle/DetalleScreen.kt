package com.jsalinas.cruzdelsurapp.presentation.screens.detalle

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Luggage
import androidx.compose.material.icons.outlined.Category
import androidx.compose.material.icons.outlined.ConfirmationNumber
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material.icons.outlined.Menu
import androidx.compose.material.icons.outlined.Scale
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import com.jsalinas.cruzdelsurapp.presentation.common.InfoCard
import com.jsalinas.cruzdelsurapp.presentation.common.LoadingOverlay
import com.jsalinas.cruzdelsurapp.presentation.common.MetaBadge

@Composable
fun DetalleScreen(
    onEditClick: (String) -> Unit,
    onEquipajeDeleted: () -> Unit,
    viewModel: DetalleViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.cargarEquipaje()
    }

    val colorAzulMarino = Color(0xFF0B2545)
    val colorGris = Color(0xFF7D7F99)
    val colorGrisOscuro = Color(0xFF555B6E)
    val colorRojo = Color(0xFFC8102E)
    val colorFondo = Color(0xFFF7F8FB)

    Box(modifier = Modifier.fillMaxSize()) {
        uiState.equipaje?.let { equipaje ->
            Column(
                modifier = Modifier.fillMaxSize().background(colorFondo)
                    .verticalScroll(rememberScrollState()).padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // 1. Ícono del equipaje
                Box(
                    modifier = Modifier.size(110.dp).background(colorAzulMarino, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Luggage,
                        contentDescription = "Equipaje",
                        tint = Color.White,
                        modifier = Modifier.size(56.dp)
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                // 2. Estado actual
                CuadroInfo(Icons.Outlined.ConfirmationNumber, "REGISTRADO EN COUNTER")
                Spacer(modifier = Modifier.height(12.dp))

                // 3. Pasajero y boleto
                Text(
                    text = equipaje.pasajero,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = colorAzulMarino
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Boleto: ${equipaje.boleto}",
                    fontSize = 14.sp,
                    color = colorGrisOscuro,
                    fontWeight = FontWeight.Medium
                )

                Spacer(modifier = Modifier.height(16.dp))

                // 4. Grid de tarjetas (Destino, Tipo, Peso)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    InfoCard(
                        icon = Icons.Outlined.LocationOn,
                        label = "Destino",
                        value = equipaje.destino,
                        modifier = Modifier.weight(1f)
                    )
                    InfoCard(
                        icon = Icons.Outlined.Scale,
                        label = "Peso",
                        value = "${equipaje.peso} kg",
                        modifier = Modifier.weight(1f)
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                InfoCard(
                    icon = Icons.Outlined.Category,
                    label = "Tipo de equipaje",
                    value = equipaje.tipo,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(16.dp))

                // 5. Observaciones
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    border = BorderStroke(1.dp, Color(0xFFEAEAF5)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Outlined.Menu,
                                contentDescription = null,
                                tint = colorAzulMarino,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Observaciones de custodia",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = colorAzulMarino
                            )
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        Text(
                            text = equipaje.observaciones.ifBlank { "Sin observaciones" },
                            fontSize = 13.sp,
                            color = colorGris,
                            lineHeight = 18.sp
                        )

                        Spacer(modifier = Modifier.height(12.dp))
                        MetaBadge(text = "Código: ${equipaje.id.take(8).uppercase()}")
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                // 6. Botones de acción (Editar / Eliminar)
                Button(
                    onClick = { onEditClick(equipaje.id) },
                    colors = ButtonDefaults.buttonColors(containerColor = colorAzulMarino),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth().height(44.dp)
                ) {
                    Icon(imageVector = Icons.Default.Edit, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = "Editar", fontWeight = FontWeight.Bold)
                }

                Spacer(modifier = Modifier.height(8.dp))

                Button(
                    onClick = { viewModel.showDeleteDialog() },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFFDCDC)),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth().height(44.dp)
                ) {
                    Icon(imageVector = Icons.Default.Delete, contentDescription = null, tint = colorRojo,
                        modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = "Eliminar", color = colorRojo, fontWeight = FontWeight.Bold)
                }
            }
        }

        LoadingOverlay(isLoading = uiState.isLoading)

        if (uiState.showDeleteDialog) {
            AlertDialog(
                onDismissRequest = { viewModel.dismissDeleteDialog() },
                title = { Text("Confirmar Eliminación") },
                text = { Text("¿Está seguro de que desea eliminar este equipaje?") },
                confirmButton = {
                    TextButton(onClick = { viewModel.confirmDelete(onEquipajeDeleted) }) {
                        Text("Eliminar", color = MaterialTheme.colorScheme.error)
                    }
                },
                dismissButton = {
                    TextButton(onClick = { viewModel.dismissDeleteDialog() }) {
                        Text("Cancelar")
                    }
                }
            )
        }
    }
}
