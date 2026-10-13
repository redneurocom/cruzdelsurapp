package com.grupo6.cruzdelsurapp.presentation.screens.form

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.automirrored.filled.Notes
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Badge
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ConfirmationNumber
import androidx.compose.material.icons.filled.NearMe
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.filled.Print
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.Save
import androidx.compose.material.icons.filled.Scale
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.OutlinedButton
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.grupo6.cruzdelsurapp.presentation.common.CustomSnackbarHost
import com.grupo6.cruzdelsurapp.presentation.common.FRANQUICIA_KG
import com.grupo6.cruzdelsurapp.presentation.common.LoadingOverlay
import com.grupo6.cruzdelsurapp.presentation.common.SeccionCard
import com.grupo6.cruzdelsurapp.presentation.common.cdsFieldColors
import com.grupo6.cruzdelsurapp.ui.theme.CdsAlert
import com.grupo6.cruzdelsurapp.ui.theme.CdsBorder
import com.grupo6.cruzdelsurapp.ui.theme.CdsField
import com.grupo6.cruzdelsurapp.ui.theme.CdsGreen
import com.grupo6.cruzdelsurapp.ui.theme.CdsGreenContainer
import com.grupo6.cruzdelsurapp.ui.theme.CdsNavy
import com.grupo6.cruzdelsurapp.ui.theme.CdsText
import com.grupo6.cruzdelsurapp.ui.theme.CdsTextMuted

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun FormScreen(onSuccess: () -> Unit, viewModel: FormViewModel = hiltViewModel()) {
    val uiState = viewModel.uiState
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(uiState.mensaje) {
        uiState.mensaje?.let {
            snackbarHostState.showSnackbar(it)
            viewModel.onMensajeMostrado()
        }
    }
    val campoShape = RoundedCornerShape(14.dp)

    Box(modifier = Modifier.fillMaxSize()){
        Column(
            modifier = Modifier.fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // Encabezado
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier.size(22.dp).background(CdsNavy, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Text("1", color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Control de bodega",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = CdsTextMuted
                )
            }
            Column {
                Text(
                    text = if (uiState.isEditing) "Editar Registro de Equipaje" else "Nuevo Registro de Equipaje",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = CdsText
                )
                Text(
                    text = "Vincule la unidad con el boleto del pasajero y verifique la franquicia de peso antes del embarque.",
                    fontSize = 13.sp,
                    color = CdsTextMuted
                )
            }

            // Boleto
            SeccionCard(
                titulo = "Número de boleto",
                icono = Icons.Default.ConfirmationNumber,
                etiqueta = if (uiState.boleto.isBlank()) "Pendiente" else "Ingresado"
            ) {
                OutlinedTextField(
                    value = uiState.boleto,
                    onValueChange = viewModel::onBoletoChange,
                    placeholder = { Text("Ej. B001-0029412") },
                    leadingIcon = { Icon(Icons.Default.ConfirmationNumber, contentDescription = null) },
                    singleLine = true,
                    shape = campoShape,
                    colors = cdsFieldColors(),
                    modifier = Modifier.fillMaxWidth()
                )
            }

            // Pasajero titular
            SeccionCard(titulo = "Pasajero titular", icono = Icons.Default.Badge) {
                OutlinedTextField(
                    value = uiState.pasajero,
                    onValueChange = viewModel::onPasajeroChange,
                    placeholder = { Text("Nombres y apellidos") },
                    leadingIcon = { Icon(Icons.Default.Badge, contentDescription = null) },
                    singleLine = true,
                    shape = campoShape,
                    colors = cdsFieldColors(),
                    modifier = Modifier.fillMaxWidth()
                )
            }

            // Destino - catálogo obtenido del servicio /destinos
            SeccionCard(titulo = "Destino final de bodega", icono = Icons.Default.NearMe) {
                if (uiState.destinos.isEmpty()) {
                    Text("Cargando destinos...", fontSize = 13.sp, color = CdsTextMuted)
                } else {
                    FlowRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        uiState.destinos.forEach { destino ->
                            val seleccionado = uiState.destino == destino.nombre
                            FilterChip(
                                selected = seleccionado,
                                onClick = { viewModel.onDestinoChange(destino.nombre) },
                                label = { Text(destino.nombre, fontWeight = FontWeight.SemiBold) },
                                leadingIcon = if (seleccionado) {
                                    { Icon(Icons.Default.Place, contentDescription = null, modifier = Modifier.size(16.dp)) }
                                } else null,
                                shape = RoundedCornerShape(50),
                                colors = FilterChipDefaults.filterChipColors(
                                    containerColor = CdsField,
                                    labelColor = CdsText,
                                    selectedContainerColor = CdsNavy,
                                    selectedLabelColor = Color.White,
                                    selectedLeadingIconColor = Color.White
                                ),
                                border = null
                            )
                        }
                    }
                }
            }

            // Peso en kg
            val peso = uiState.pesoKg.toIntOrNull() ?: 0
            val dentroLimite = peso <= FRANQUICIA_KG
            SeccionCard(
                titulo = "Peso en bodega",
                icono = Icons.Default.Scale,
                etiqueta = "Franquicia: máx. $FRANQUICIA_KG kg"
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    FilledTonalIconButton(
                        onClick = { viewModel.onPesoChange((peso - 1).coerceAtLeast(0).toString()) },
                        colors = IconButtonDefaults.filledTonalIconButtonColors(containerColor = CdsField, contentColor = CdsNavy)
                    ) { Icon(Icons.Default.Remove, contentDescription = "Restar") }

                    OutlinedTextField(
                        value = uiState.pesoKg,
                        onValueChange = viewModel::onPesoChange,
                        placeholder = { Text("0", modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center) },
                        suffix = { Text("kg", fontWeight = FontWeight.Bold, color = CdsTextMuted) },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        singleLine = true,
                        textStyle = TextStyle(
                            fontSize = 28.sp,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center,
                            color = CdsText
                        ),
                        shape = campoShape,
                        colors = cdsFieldColors(),
                        modifier = Modifier.weight(1f).padding(horizontal = 8.dp)
                    )

                    FilledTonalIconButton(
                        onClick = { viewModel.onPesoChange((peso + 1).toString()) },
                        colors = IconButtonDefaults.filledTonalIconButtonColors(containerColor = CdsField, contentColor = CdsNavy)
                    ) { Icon(Icons.Default.Add, contentDescription = "Sumar") }
                }

                Spacer(modifier = Modifier.height(10.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        if (dentroLimite) Icons.Default.CheckCircle else Icons.Default.Warning,
                        contentDescription = null,
                        tint = if (dentroLimite) CdsGreen else CdsAlert,
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = if (dentroLimite) "Dentro del límite permitido" else "Excede la franquicia de peso",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = if (dentroLimite) CdsGreen else CdsAlert
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
                LinearProgressIndicator(
                    progress = { (peso.toFloat() / FRANQUICIA_KG).coerceIn(0f, 1f) },
                    color = if (dentroLimite) CdsGreen else CdsAlert,
                    trackColor = CdsBorder,
                    modifier = Modifier.fillMaxWidth().height(8.dp).clip(RoundedCornerShape(50))
                )
                Spacer(modifier = Modifier.height(4.dp))
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text("0 kg", fontSize = 11.sp, color = CdsTextMuted)
                    Text("$peso / $FRANQUICIA_KG kg", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = CdsText)
                    Text("Límite $FRANQUICIA_KG kg", fontSize = 11.sp, color = CdsTextMuted)
                }
            }

            // Observaciones de custodia
            SeccionCard(
                titulo = "Observaciones de custodia",
                icono = Icons.AutoMirrored.Filled.Notes,
                etiqueta = "Detalle"
            ) {
                OutlinedTextField(
                    value = uiState.observaciones,
                    onValueChange = viewModel::onObservacionesChange,
                    placeholder = { Text("Ej. Frágil, con funda protectora") },
                    shape = campoShape,
                    colors = cdsFieldColors(),
                    modifier = Modifier.fillMaxWidth(),
                    minLines = 3
                )
                Spacer(modifier = Modifier.height(10.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    listOf("Frágil", "Embalado", "Candado TSA").forEach { etiqueta ->
                        Surface(
                            onClick = {
                                val actual = uiState.observaciones.trim()
                                if (!actual.contains(etiqueta, ignoreCase = true)) {
                                    viewModel.onObservacionesChange(
                                        if (actual.isEmpty()) etiqueta else "$actual, $etiqueta"
                                    )
                                }
                            },
                            shape = RoundedCornerShape(8.dp),
                            color = CdsField
                        ) {
                            Text(
                                text = "+ $etiqueta",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = CdsTextMuted,
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                            )
                        }
                    }
                }
            }

            // Acciones
            Button(
                onClick = { viewModel.guardarEquipaje(onSuccess) },
                colors = ButtonDefaults.buttonColors(containerColor = CdsNavy),
                shape = RoundedCornerShape(50),
                modifier = Modifier.fillMaxWidth().height(56.dp)
            ) {
                Icon(
                    if (uiState.isEditing) Icons.Default.Save else Icons.Default.Print,
                    contentDescription = null,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    if (uiState.isEditing) "Actualizar registro" else "Registrar equipaje",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.width(8.dp))
                Icon(Icons.AutoMirrored.Filled.ArrowForward, contentDescription = null, modifier = Modifier.size(18.dp))
            }

            if (uiState.isEditing){
                OutlinedButton(
                    onClick = { viewModel.cancelar(onSuccess) },
                    shape = RoundedCornerShape(50),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = CdsNavy),
                    modifier = Modifier.fillMaxWidth().height(50.dp)
                ) {
                    Text("Cancelar", fontWeight = FontWeight.Bold)
                }
            }

            Surface(shape = RoundedCornerShape(12.dp), color = CdsGreenContainer, modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = "Verifique el boleto y el peso antes de confirmar. El registro queda disponible en el manifiesto de bodega.",
                    fontSize = 12.sp,
                    color = CdsGreen,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(12.dp)
                )
            }
        }
        LoadingOverlay(isLoading = uiState.isLoading)

        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.BottomCenter) {
            CustomSnackbarHost(hostState = snackbarHostState, currentType = uiState.tipoMensaje)
        }
    }
}
