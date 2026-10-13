package com.grupo6.cruzdelsurapp.presentation.screens.detalle

import android.content.ClipData
import android.content.ClipboardManager
import androidx.compose.foundation.BorderStroke
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Notes
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.ConfirmationNumber
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.DirectionsBus
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.NearMe
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.filled.Scale
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.grupo6.cruzdelsurapp.presentation.common.CuadroInfo
import com.grupo6.cruzdelsurapp.presentation.common.CustomSnackbarHost
import com.grupo6.cruzdelsurapp.presentation.common.FRANQUICIA_KG
import com.grupo6.cruzdelsurapp.presentation.common.InfoCard
import com.grupo6.cruzdelsurapp.presentation.common.MetaBadge
import com.grupo6.cruzdelsurapp.presentation.common.codigoRastreo
import com.grupo6.cruzdelsurapp.presentation.common.iniciales
import com.grupo6.cruzdelsurapp.ui.theme.CdsAlert
import com.grupo6.cruzdelsurapp.ui.theme.CdsAlertContainer
import com.grupo6.cruzdelsurapp.ui.theme.CdsBorder
import com.grupo6.cruzdelsurapp.ui.theme.CdsField
import com.grupo6.cruzdelsurapp.ui.theme.CdsGreenDark
import com.grupo6.cruzdelsurapp.ui.theme.CdsNavy
import com.grupo6.cruzdelsurapp.ui.theme.CdsNavyContainer
import com.grupo6.cruzdelsurapp.ui.theme.CdsText
import com.grupo6.cruzdelsurapp.ui.theme.CdsTextMuted

@Composable
fun DetalleScreen(
    onEditClick: (String) -> Unit,
    onEquipajeDeleted: () -> Unit,
    viewModel: DetalleViewModel = hiltViewModel()
) {
    val uiState = viewModel.uiState
    val context = LocalContext.current
    var copiado by remember { mutableStateOf(false) }
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(Unit) {
        viewModel.cargarEquipaje()
    }

    LaunchedEffect(uiState.mensaje) {
        uiState.mensaje?.let {
            snackbarHostState.showSnackbar(it)
            viewModel.onMensajeMostrado()
        }
    }

    uiState.equipaje?.let { equipaje ->
        val codigo = codigoRastreo(equipaje)
        val dentroLimite = equipaje.pesoKg <= FRANQUICIA_KG

        Column(
            modifier = Modifier.fillMaxSize()
                .verticalScroll(rememberScrollState()).padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // 1. Estado de custodia
            CuadroInfo(Icons.Default.Shield, "CUSTODIA ACTIVA")
            Spacer(modifier = Modifier.height(12.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(20.dp))
                    .background(Brush.linearGradient(listOf(CdsGreenDark, Color(0xFF14532D))))
                    .padding(18.dp)
            ) {
                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Surface(shape = CircleShape, color = Color(0xFF4ADE80)) {
                            Text(
                                text = "EN BODEGA • LISTO PARA EMBARQUE",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = CdsGreenDark,
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                            )
                        }
                        Spacer(modifier = Modifier.weight(1f))
                        Icon(Icons.Default.DirectionsBus, contentDescription = null, tint = Color(0xFF4ADE80))
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.NearMe, contentDescription = null, tint = Color(0xFF4ADE80), modifier = Modifier.size(20.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = equipaje.destino.ifBlank { "Sin destino" },
                            color = Color(0xFF4ADE80),
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Text(
                        text = "Destino final de la unidad",
                        color = Color.White.copy(alpha = 0.8f),
                        fontSize = 13.sp
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    // Etapas de custodia
                    Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                        EtapaCustodia("Registro", completada = true)
                        LineaEtapa(completada = true, modifier = Modifier.weight(1f))
                        EtapaCustodia("Bodega", completada = true)
                        LineaEtapa(completada = false, modifier = Modifier.weight(1f))
                        EtapaCustodia("Entrega", completada = false)
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // 2. Ficha de la unidad
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                border = BorderStroke(1.dp, CdsBorder),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    // Código de rastreo
                    Surface(shape = RoundedCornerShape(14.dp), color = CdsField, modifier = Modifier.fillMaxWidth()) {
                        Row(
                            modifier = Modifier.padding(14.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text("CÓDIGO DE RASTREO", fontSize = 11.sp, fontWeight = FontWeight.SemiBold, color = CdsTextMuted)
                                Text(
                                    text = codigo,
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.Bold,
                                    fontFamily = FontFamily.Monospace,
                                    color = CdsText,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }
                            Button(
                                onClick = {
                                    context.getSystemService(ClipboardManager::class.java)
                                        ?.setPrimaryClip(ClipData.newPlainText("Código de rastreo", codigo))
                                    copiado = true
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = CdsNavyContainer, contentColor = CdsNavy),
                                shape = RoundedCornerShape(12.dp),
                                contentPadding = PaddingValues(horizontal = 12.dp, vertical = 0.dp),
                                modifier = Modifier.height(36.dp)
                            ) {
                                Icon(
                                    if (copiado) Icons.Default.Check else Icons.Default.ContentCopy,
                                    contentDescription = null,
                                    modifier = Modifier.size(15.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(if (copiado) "Copiado" else "Copiar", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Pasajero titular
                    Surface(shape = RoundedCornerShape(14.dp), color = CdsField, modifier = Modifier.fillMaxWidth()) {
                        Row(modifier = Modifier.padding(14.dp), verticalAlignment = Alignment.CenterVertically) {
                            Surface(shape = CircleShape, color = CdsNavyContainer, modifier = Modifier.size(44.dp)) {
                                Box(contentAlignment = Alignment.Center) {
                                    Text(iniciales(equipaje.pasajero), fontWeight = FontWeight.Bold, color = CdsNavy)
                                }
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text("Pasajero titular", fontSize = 12.sp, color = CdsTextMuted)
                                Text(
                                    text = equipaje.pasajero,
                                    fontSize = 17.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = CdsText
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Boleto y peso
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        InfoCard(
                            icon = Icons.Default.ConfirmationNumber,
                            label = "Boleto electrónico",
                            value = equipaje.boleto,
                            modifier = Modifier.weight(1f)
                        )
                        InfoCard(
                            icon = Icons.Default.Scale,
                            label = "Peso en bodega",
                            value = "${equipaje.pesoKg} kg",
                            modifier = Modifier.weight(1f)
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    InfoCard(
                        icon = Icons.Default.Place,
                        label = "Destino final",
                        value = equipaje.destino.ifBlank { "Sin destino" },
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    Text("ESPECIFICACIONES DE CARGA", fontSize = 11.sp, fontWeight = FontWeight.SemiBold, color = CdsTextMuted)
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        MetaBadge(text = "${equipaje.pesoKg} kg")
                        MetaBadge(text = if (dentroLimite) "Dentro de franquicia" else "Excede franquicia")
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // 3. Observaciones de custodia
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                border = BorderStroke(1.dp, CdsBorder),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.Notes,
                                contentDescription = null,
                                tint = CdsNavy,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Observaciones de custodia",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = CdsText
                            )
                        }
                        Text(
                            text = "REGISTRO",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = CdsTextMuted
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = equipaje.observaciones.ifBlank { "Sin observaciones registradas." },
                        fontSize = 14.sp,
                        color = CdsTextMuted,
                        lineHeight = 20.sp,
                        textAlign = TextAlign.Start
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // 4. Botones de acción (solo operario con sesión)
            if (uiState.puedeEditar) {
                Button(
                    onClick = { onEditClick(equipaje.id) },
                    colors = ButtonDefaults.buttonColors(containerColor = CdsNavy),
                    shape = RoundedCornerShape(50),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = "Editar registro", fontWeight = FontWeight.Bold, fontSize = 15.sp)
                }

                Spacer(modifier = Modifier.height(10.dp))

                Button(
                    onClick = { viewModel.showDeleteDialog() },
                    colors = ButtonDefaults.buttonColors(containerColor = CdsAlertContainer),
                    shape = RoundedCornerShape(50),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Delete, contentDescription = null, tint = CdsAlert,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = "Eliminar registro", color = CdsAlert, fontWeight = FontWeight.Bold, fontSize = 15.sp)
                }
            }
        }
    }

    if (uiState.showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { viewModel.dismissDeleteDialog() },
            containerColor = Color.White,
            icon = { Icon(Icons.Default.Delete, contentDescription = null, tint = CdsAlert) },
            title = { Text("Eliminar registro", fontWeight = FontWeight.Bold) },
            text = { Text("¿Está seguro de que desea eliminar este registro de equipaje? Esta acción no se puede deshacer.") },
            confirmButton = {
                TextButton(onClick = { viewModel.confirmDelete(onEquipajeDeleted) }) {
                    Text("Eliminar", color = CdsAlert, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { viewModel.dismissDeleteDialog() }) {
                    Text("Cancelar", color = CdsNavy)
                }
            }
        )
    }

    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.BottomCenter) {
        CustomSnackbarHost(hostState = snackbarHostState, currentType = uiState.tipoMensaje)
    }
}

@Composable
private fun EtapaCustodia(texto: String, completada: Boolean) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box(
            modifier = Modifier
                .size(22.dp)
                .background(if (completada) Color(0xFF4ADE80) else Color.White.copy(alpha = 0.2f), CircleShape),
            contentAlignment = Alignment.Center
        ) {
            if (completada) {
                Icon(Icons.Default.Check, contentDescription = null, tint = CdsGreenDark, modifier = Modifier.size(14.dp))
            }
        }
        Spacer(modifier = Modifier.height(4.dp))
        Text(texto, fontSize = 11.sp, color = Color.White.copy(alpha = if (completada) 1f else 0.6f))
    }
}

@Composable
private fun LineaEtapa(completada: Boolean, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .padding(horizontal = 6.dp)
            .padding(bottom = 18.dp)
            .height(3.dp)
            .background(
                if (completada) Color(0xFF4ADE80) else Color.White.copy(alpha = 0.2f),
                RoundedCornerShape(50)
            )
    )
}
