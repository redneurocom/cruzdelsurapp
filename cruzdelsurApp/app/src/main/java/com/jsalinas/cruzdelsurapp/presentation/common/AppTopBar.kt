package com.jsalinas.cruzdelsurapp.presentation.common

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.jsalinas.cruzdelsurapp.R
import com.jsalinas.cruzdelsurapp.presentation.navigation.BottomNavItem
import com.jsalinas.cruzdelsurapp.presentation.navigation.RutasNav

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppTopBar(navController: NavHostController){
    val navBackStackEntry = navController.currentBackStackEntryAsState()
    val ruta = navBackStackEntry.value?.destination?.route
    val titulo = RutasNav.getTitulo(ruta)
    val mostrarBotonBack = ruta == RutasNav.DETALLE


    Column(modifier = Modifier.statusBarsPadding()){
        Row(
            modifier = Modifier.fillMaxWidth()
                .padding(start = 16.dp, end = 16.dp, bottom = 10.dp, top = 20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if(mostrarBotonBack){
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "",
                        tint = Color(0xFF0B2545),
                        modifier = Modifier.size(28.dp))
                }
            }else{
                Image(
                    painter = painterResource(id = R.drawable.ic_logo_cruz),
                    contentDescription = "Logo",
                    modifier = Modifier.size(36.dp)
                        .background(Color(0xFF0B2545), RoundedCornerShape(8.dp))
                        .padding(2.dp)
                )
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text(text = "Cruz del Sur", color = Color(0xFF555B6E), fontSize = 12.sp)
                Text(
                    text = titulo,
                    color = Color(0xFF0B2545),
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            // Espaciador para empujar los botones de acción a la derecha
            Spacer(modifier = Modifier.weight(1f))

            // Botón de búsqueda
            IconButton(onClick = { navController.navigate(BottomNavItem.Buscar.route) }) {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Buscar",
                    tint = Color(0xFF0B2545)
                )
            }

            // Botón de cerrar sesión
            IconButton(onClick = {
                navController.navigate(RutasNav.LOGIN) {
                    popUpTo(navController.graph.id) { inclusive = true }
                }
            }) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.Logout,
                    contentDescription = "Cerrar sesión",
                    tint = Color(0xFF0B2545)
                )
            }
        }

        HorizontalDivider(thickness = 2.dp, color = Color(0xFFC8C4D5))
    }
}