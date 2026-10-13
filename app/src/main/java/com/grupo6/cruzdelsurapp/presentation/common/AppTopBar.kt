package com.grupo6.cruzdelsurapp.presentation.common

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.grupo6.cruzdelsurapp.domain.model.Usuario
import com.grupo6.cruzdelsurapp.presentation.navigation.RutasNav
import com.grupo6.cruzdelsurapp.ui.theme.CdsAlert
import com.grupo6.cruzdelsurapp.ui.theme.CdsBorder
import com.grupo6.cruzdelsurapp.ui.theme.CdsNavy
import com.grupo6.cruzdelsurapp.ui.theme.CdsNavyContainer
import com.grupo6.cruzdelsurapp.ui.theme.CdsText
import com.grupo6.cruzdelsurapp.ui.theme.CdsTextMuted

/** Texto legible del rol guardado en la tabla cds-usuarios. */
fun nombreRol(rol: String): String = when (rol) {
    "OPERARIO_COUNTER" -> "Operario de counter"
    "OPERARIO_BODEGA" -> "Operario de bodega"
    "SUPERVISOR" -> "Supervisor"
    else -> rol.lowercase().replace('_', ' ').replaceFirstChar { it.uppercase() }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppTopBar(
    navController: NavHostController,
    usuario: Usuario?,
    onCerrarSesion: () -> Unit
){
    val navBackStackEntry = navController.currentBackStackEntryAsState()
    val ruta = navBackStackEntry.value?.destination?.route
    val titulo = RutasNav.getTitulo(ruta)
    // Detalle siempre vuelve; el pasajero (sin sesión) vuelve al login desde la consulta
    val mostrarBotonBack = ruta == RutasNav.DETALLE || (usuario == null && ruta == RutasNav.BUSCAR)
    var menuAbierto by remember { mutableStateOf(false) }

    Column(modifier = Modifier.background(Color.White).statusBarsPadding()){
        Row(
            modifier = Modifier.fillMaxWidth()
                .padding(start = 12.dp, end = 12.dp, bottom = 10.dp, top = 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if(mostrarBotonBack){
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Volver",
                        tint = CdsNavy,
                        modifier = Modifier.size(26.dp))
                }
            }else{
                Spacer(modifier = Modifier.width(4.dp))
                LogoCds(size = 40.dp)
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = if (usuario == null) "Cruz del Sur • Pasajero" else "Cruz del Sur",
                    color = CdsTextMuted,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium
                )
                Text(
                    text = titulo,
                    color = CdsText,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            if (usuario != null) {
                // Búsqueda: abre la consulta de equipaje
                IconButton(onClick = {
                    navController.navigate(RutasNav.BUSCAR) {
                        popUpTo(RutasNav.INICIO) { saveState = true }
                        launchSingleTop = true
                        restoreState = true
                    }
                }) {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Buscar",
                        tint = CdsTextMuted
                    )
                }

                // Perfil del operario con opción de cerrar sesión
                Box {
                    IconButton(onClick = { menuAbierto = true }) {
                        Surface(shape = CircleShape, color = CdsNavyContainer, modifier = Modifier.size(34.dp)) {
                            Box(contentAlignment = Alignment.Center) {
                                Text(
                                    text = iniciales(usuario.nombre),
                                    color = CdsNavy,
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                    DropdownMenu(
                        expanded = menuAbierto,
                        onDismissRequest = { menuAbierto = false },
                        containerColor = Color.White
                    ) {
                        Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)) {
                            Text(usuario.nombre, fontWeight = FontWeight.Bold, color = CdsText)
                            Text("${usuario.codigo} • ${nombreRol(usuario.rol)}", fontSize = 12.sp, color = CdsTextMuted)
                            if (usuario.terminal.isNotBlank()) {
                                Text(usuario.terminal, fontSize = 12.sp, color = CdsTextMuted)
                            }
                        }
                        HorizontalDivider(color = CdsBorder)
                        DropdownMenuItem(
                            text = { Text("Cerrar sesión", color = CdsAlert, fontWeight = FontWeight.SemiBold) },
                            leadingIcon = { Icon(Icons.AutoMirrored.Filled.Logout, contentDescription = null, tint = CdsAlert) },
                            onClick = {
                                menuAbierto = false
                                onCerrarSesion()
                            }
                        )
                    }
                }
            }
        }

        HorizontalDivider(thickness = 1.dp, color = CdsBorder)
    }
}
