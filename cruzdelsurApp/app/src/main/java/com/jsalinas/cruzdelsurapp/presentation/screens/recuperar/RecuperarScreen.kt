package com.jsalinas.cruzdelsurapp.presentation.screens.recuperar

import androidx.compose.foundation.background
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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Key
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel

@Composable
fun RecuperarScreen(
    onVolver: () -> Unit,
    viewModel: RecuperarViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    val colorAzulMarino = Color(0xFF0B2545)
    val colorGrisOscuro = Color(0xFF555B6E)
    val colorVerde = Color(0xFF1B8A5A)

    Column(
        modifier = Modifier.fillMaxSize().background(Color(0xFFF7F8FB))
            .verticalScroll(rememberScrollState()).padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(modifier = Modifier.fillMaxWidth()) {
            IconButton(onClick = onVolver) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver", tint = colorAzulMarino)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // 1. Ícono principal
        Box(
            modifier = Modifier.size(96.dp).background(colorAzulMarino, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(Icons.Outlined.Key, contentDescription = null, tint = Color.White, modifier = Modifier.size(44.dp))
        }

        Spacer(modifier = Modifier.height(20.dp))

        // 2. Título y descripción
        Text(
            text = "Recuperación de Contraseña",
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            color = colorAzulMarino
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Ingresa tu correo institucional asignado para recibir las instrucciones y el enlace de restablecimiento temporal.",
            fontSize = 14.sp,
            color = colorGrisOscuro,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(24.dp))

        // 3. Correo
        OutlinedTextField(
            value = uiState.correo,
            onValueChange = viewModel::onCorreoChange,
            label = { Text("Correo institucional") },
            placeholder = { Text("ejemplo@cruzdelsur.com.pe") },
            leadingIcon = { Icon(Icons.Outlined.Email, contentDescription = null) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = viewModel::enviarInstrucciones,
            colors = ButtonDefaults.buttonColors(containerColor = colorAzulMarino),
            shape = RoundedCornerShape(28.dp),
            modifier = Modifier.fillMaxWidth().height(52.dp)
        ) {
            Text("Enviar instrucciones", fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.width(8.dp))
            Icon(Icons.AutoMirrored.Filled.Send, contentDescription = null)
        }

        // 4. Mensaje de confirmación
        if (uiState.enviado) {
            Spacer(modifier = Modifier.height(16.dp))
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFF2F4F7)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.CheckCircle, contentDescription = null, tint = colorVerde, modifier = Modifier.size(32.dp))
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text("Validación de Credenciales", fontWeight = FontWeight.Bold, color = colorAzulMarino)
                        Text(
                            text = "Si tu usuario está registrado, recibirás un enlace válido por 15 minutos.",
                            fontSize = 13.sp,
                            color = colorGrisOscuro
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        TextButton(onClick = onVolver) {
            Text("Volver al Inicio de Sesión", color = colorAzulMarino)
        }
    }
}
