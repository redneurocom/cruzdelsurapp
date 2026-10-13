package com.jsalinas.cruzdelsurapp.presentation.screens.form

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuAnchorType
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.jsalinas.cruzdelsurapp.presentation.common.LoadingOverlay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FormScreen(onSuccess: () -> Unit, viewModel: FormViewModel = hiltViewModel()) {
    val uiState by viewModel.uiState.collectAsState()
    var expandedDestino by remember { mutableStateOf(false) }
    var expandedTipo by remember { mutableStateOf(false) }

    Box(modifier = Modifier.fillMaxSize()){
        Column(
            modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = if (uiState.isEditing) "Editar Equipaje" else "Nuevo Registro de Equipaje",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF0B2545)
            )

            OutlinedTextField(
                value = uiState.boleto,
                onValueChange = viewModel::onBoletoChange,
                label = { Text("Número de boleto") },
                placeholder = { Text("B001-0029412") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = uiState.pasajero,
                onValueChange = viewModel::onPasajeroChange,
                label = { Text("Nombre del pasajero") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            ExposedDropdownMenuBox(
                expanded = expandedDestino,
                onExpandedChange = { expandedDestino = !expandedDestino },
                modifier = Modifier.fillMaxWidth()
            ){
                OutlinedTextField(
                    value = uiState.destino,
                    onValueChange = {},
                    label = { Text("Destino") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedDestino)},
                    colors = ExposedDropdownMenuDefaults.outlinedTextFieldColors(),
                    modifier = Modifier.menuAnchor(type = ExposedDropdownMenuAnchorType.PrimaryNotEditable, enabled = true)
                        .fillMaxWidth()
                )

                ExposedDropdownMenu(
                    expanded = expandedDestino,
                    onDismissRequest = {expandedDestino = false}
                ) {
                    uiState.destinos.forEach { destino ->
                        DropdownMenuItem(
                            text = { Text(destino.nombre) },
                            onClick = {
                                viewModel.onDestinoChange(destino.nombre)
                                expandedDestino = false
                            }
                        )
                    }
                }
            }

            ExposedDropdownMenuBox(
                expanded = expandedTipo,
                onExpandedChange = { expandedTipo = !expandedTipo },
                modifier = Modifier.fillMaxWidth()
            ){
                OutlinedTextField(
                    value = uiState.tipo,
                    onValueChange = {},
                    label = { Text("Tipo de equipaje") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedTipo)},
                    colors = ExposedDropdownMenuDefaults.outlinedTextFieldColors(),
                    modifier = Modifier.menuAnchor(type = ExposedDropdownMenuAnchorType.PrimaryNotEditable, enabled = true)
                        .fillMaxWidth()
                )

                ExposedDropdownMenu(
                    expanded = expandedTipo,
                    onDismissRequest = {expandedTipo = false}
                ) {
                    uiState.tipos.forEach { tipo ->
                        DropdownMenuItem(
                            text = { Text(tipo) },
                            onClick = {
                                viewModel.onTipoChange(tipo)
                                expandedTipo = false
                            }
                        )
                    }
                }
            }

            OutlinedTextField(
                value = uiState.peso,
                onValueChange = viewModel::onPesoChange,
                label = { Text("Peso en bodega (kg)") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = uiState.observaciones,
                onValueChange = viewModel::onObservacionesChange,
                label = { Text("Observaciones (opcional)") },
                modifier = Modifier.fillMaxWidth(),
                minLines = 3
            )

            Button(
                onClick = { viewModel.guardarEquipaje(onSuccess) },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0B2545)),
                shape = RoundedCornerShape(28.dp),
                modifier = Modifier.fillMaxWidth().height(52.dp)
            ) {
                Text(if (uiState.isEditing) "Actualizar" else "Registrar equipaje", fontWeight = FontWeight.Bold)
            }

            if (uiState.isEditing){
                OutlinedButton(
                    onClick = { viewModel.cancelar(onSuccess) },
                    shape = RoundedCornerShape(28.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text( "Cancelar")
                }
            }
        }
        LoadingOverlay(isLoading = uiState.isLoading)
    }
}
