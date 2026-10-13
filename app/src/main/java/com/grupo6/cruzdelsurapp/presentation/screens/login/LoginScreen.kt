package com.grupo6.cruzdelsurapp.presentation.screens.login

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
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Login
import androidx.compose.material.icons.filled.Badge
import androidx.compose.material.icons.filled.Key
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Luggage
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Terminal
import androidx.compose.material.icons.filled.TravelExplore
import androidx.compose.material.icons.filled.VerifiedUser
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.grupo6.cruzdelsurapp.presentation.common.CustomSnackbarHost
import com.grupo6.cruzdelsurapp.presentation.common.LoadingOverlay
import com.grupo6.cruzdelsurapp.presentation.common.LogoCds
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

/** Pantalla 1 del prototipo: Inicio de sesión del operario. */
@Composable
fun LoginScreen(
    onLoginExitoso: () -> Unit,
    onConsultarEquipaje: () -> Unit,
    onRecuperarPassword: () -> Unit,
    viewModel: LoginViewModel = hiltViewModel()
) {
    val uiState = viewModel.uiState
    val campoShape = RoundedCornerShape(14.dp)
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(uiState.mensaje) {
        uiState.mensaje?.let {
            snackbarHostState.showSnackbar(it)
            viewModel.onMensajeMostrado()
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp, vertical = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Marca
            Card(
                shape = RoundedCornerShape(22.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                border = BorderStroke(1.dp, CdsBorder),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth().padding(18.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Row(
                        modifier = Modifier
                            .background(Brush.linearGradient(listOf(CdsNavy, CdsNavyLight)), RoundedCornerShape(14.dp))
                            .padding(horizontal = 16.dp, vertical = 10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        LogoCds(size = 36.dp)
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text("CRUZ DEL SUR", color = Color.White, fontSize = 17.sp, fontWeight = FontWeight.Bold)
                            Text(
                                "MI EQUIPAJE • RASTREO",
                                color = Color.White.copy(alpha = 0.75f),
                                fontSize = 10.sp,
                                fontWeight = FontWeight.SemiBold,
                                letterSpacing = 1.sp
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                    Surface(shape = CircleShape, color = CdsField) {
                        Row(
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 5.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(modifier = Modifier.size(7.dp).background(CdsGreen, CircleShape))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                "TRAZABILIDAD DE EQUIPAJE DE BODEGA",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = CdsTextMuted,
                                letterSpacing = 0.5.sp
                            )
                        }
                    }
                }
            }

            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    "Control y Rastreo de Equipaje",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = CdsText,
                    textAlign = TextAlign.Center
                )
                Text(
                    "Ingresa con tus credenciales de colaborador",
                    fontSize = 14.sp,
                    color = CdsTextMuted,
                    textAlign = TextAlign.Center
                )
            }

            // Acceso del operario
            Card(
                shape = RoundedCornerShape(22.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                border = BorderStroke(1.dp, CdsBorder),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Surface(shape = RoundedCornerShape(12.dp), color = CdsField, modifier = Modifier.fillMaxWidth()) {
                        Row(
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 10.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(Icons.Default.Terminal, contentDescription = null, tint = CdsTextMuted, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                "Acceso Tripulación y Bodega",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = CdsText
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))
                    EtiquetaCampo(Icons.Default.Badge, "Usuario / Código de colaborador")
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = uiState.codigo,
                        onValueChange = viewModel::onCodigoChange,
                        placeholder = { Text("Ej. OP-4821") },
                        leadingIcon = { Icon(Icons.Default.Person, contentDescription = null) },
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(
                            capitalization = KeyboardCapitalization.Characters,
                            imeAction = ImeAction.Next
                        ),
                        shape = campoShape,
                        colors = cdsFieldColors(),
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(14.dp))
                    EtiquetaCampo(Icons.Default.Lock, "Contraseña")
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = uiState.password,
                        onValueChange = viewModel::onPasswordChange,
                        placeholder = { Text("Contraseña") },
                        leadingIcon = { Icon(Icons.Default.Key, contentDescription = null) },
                        trailingIcon = {
                            IconButton(onClick = viewModel::onTogglePasswordVisible) {
                                Icon(
                                    if (uiState.passwordVisible) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                                    contentDescription = if (uiState.passwordVisible) "Ocultar contraseña" else "Mostrar contraseña"
                                )
                            }
                        },
                        visualTransformation = if (uiState.passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password, imeAction = ImeAction.Done),
                        keyboardActions = KeyboardActions(onDone = { viewModel.iniciarSesion(onLoginExitoso) }),
                        shape = campoShape,
                        colors = cdsFieldColors(),
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(20.dp))
                    Button(
                        onClick = { viewModel.iniciarSesion(onLoginExitoso) },
                        enabled = !uiState.isLoading,
                        colors = ButtonDefaults.buttonColors(containerColor = CdsNavy),
                        shape = RoundedCornerShape(50),
                        modifier = Modifier.fillMaxWidth().height(54.dp)
                    ) {
                        Icon(Icons.AutoMirrored.Filled.Login, contentDescription = null, modifier = Modifier.size(20.dp))
                        Spacer(modifier = Modifier.width(10.dp))
                        Text("Ingresar", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                    }

                    TextButton(
                        onClick = onRecuperarPassword,
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    ) {
                        Text("¿Olvidaste tu contraseña?", color = CdsNavy, fontWeight = FontWeight.SemiBold)
                    }
                }
            }

            // Separador
            Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                HorizontalDivider(modifier = Modifier.weight(1f), color = CdsBorder)
                Text(
                    "O SI ERES CLIENTE",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = CdsTextMuted,
                    letterSpacing = 1.sp,
                    modifier = Modifier.padding(horizontal = 12.dp)
                )
                HorizontalDivider(modifier = Modifier.weight(1f), color = CdsBorder)
            }

            // Acceso del pasajero (sin login)
            Card(
                shape = RoundedCornerShape(22.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                border = BorderStroke(1.dp, CdsBorder),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.Top) {
                        Surface(shape = CircleShape, color = CdsNavyContainer, modifier = Modifier.size(44.dp)) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(Icons.Default.Luggage, contentDescription = null, tint = CdsNavy)
                            }
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text("¿Viajas con Cruz del Sur?", fontSize = 17.sp, fontWeight = FontWeight.Bold, color = CdsText)
                            Text(
                                "Consulta el estado de tu equipaje con el nombre del pasajero o tu número de boleto.",
                                fontSize = 13.sp,
                                color = CdsTextMuted,
                                lineHeight = 18.sp
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(14.dp))
                    Button(
                        onClick = onConsultarEquipaje,
                        colors = ButtonDefaults.buttonColors(containerColor = CdsField, contentColor = CdsNavy),
                        shape = RoundedCornerShape(50),
                        modifier = Modifier.fillMaxWidth().height(50.dp)
                    ) {
                        Icon(Icons.Default.TravelExplore, contentDescription = null, modifier = Modifier.size(20.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Consultar mi equipaje", fontWeight = FontWeight.Bold)
                    }
                }
            }

            // Pie
            Surface(shape = CircleShape, color = CdsGreenContainer) {
                Row(
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.Default.VerifiedUser, contentDescription = null, tint = CdsGreen, modifier = Modifier.size(14.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Conexión segura (HTTPS)", fontSize = 12.sp, color = CdsGreen, fontWeight = FontWeight.SemiBold)
                }
            }
            Text(
                "v1.0 • Cruz del Sur • Grupo 6",
                fontSize = 11.sp,
                color = CdsTextMuted
            )
        }

        LoadingOverlay(isLoading = uiState.isLoading)

        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.BottomCenter) {
            CustomSnackbarHost(hostState = snackbarHostState, currentType = uiState.tipoMensaje)
        }
    }
}

@Composable
private fun EtiquetaCampo(icono: ImageVector, texto: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(icono, contentDescription = null, tint = CdsText, modifier = Modifier.size(18.dp))
        Spacer(modifier = Modifier.width(8.dp))
        Text(texto, fontSize = 14.sp, fontWeight = FontWeight.Bold, color = CdsText)
    }
}
