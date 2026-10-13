package com.grupo6.cruzdelsurapp.presentation.common

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.grupo6.cruzdelsurapp.R
import com.grupo6.cruzdelsurapp.domain.model.Equipaje
import com.grupo6.cruzdelsurapp.ui.theme.CdsAlert
import com.grupo6.cruzdelsurapp.ui.theme.CdsAlertContainer
import com.grupo6.cruzdelsurapp.ui.theme.CdsBorder
import com.grupo6.cruzdelsurapp.ui.theme.CdsField
import com.grupo6.cruzdelsurapp.ui.theme.CdsGreen
import com.grupo6.cruzdelsurapp.ui.theme.CdsGreenContainer
import com.grupo6.cruzdelsurapp.ui.theme.CdsNavy
import com.grupo6.cruzdelsurapp.ui.theme.CdsText
import com.grupo6.cruzdelsurapp.ui.theme.CdsTextMuted

// Componentes visuales de Cruz del Sur (solo presentación)

const val FRANQUICIA_KG = 20

// Código de rastreo legible a partir del id del registro
fun codigoRastreo(equipaje: Equipaje): String {
    val destino = equipaje.destino.filter { it.isLetter() }.take(3).uppercase().ifEmpty { "GEN" }
    val sufijo = equipaje.id.filter { it.isLetterOrDigit() }.takeLast(6).uppercase().ifEmpty { "000000" }
    return "CDS-$destino-$sufijo"
}

fun iniciales(nombre: String): String =
    nombre.trim().split(" ").filter { it.isNotBlank() }.take(2)
        .joinToString("") { it.first().uppercase() }.ifEmpty { "?" }

@Composable
fun LogoCds(size: Dp = 40.dp) {
    Surface(
        shape = RoundedCornerShape(12.dp),
        color = CdsNavy,
        modifier = Modifier.size(size)
    ) {
        Box(contentAlignment = Alignment.Center) {
            Image(
                painter = painterResource(id = R.drawable.logo_cds),
                contentDescription = "Cruz del Sur",
                modifier = Modifier.size(size * 0.65f)
            )
        }
    }
}

/** Tarjeta blanca con encabezado (icono + título + etiqueta opcional). */
@Composable
fun SeccionCard(
    titulo: String,
    icono: ImageVector,
    modifier: Modifier = Modifier,
    etiqueta: String? = null,
    content: @Composable ColumnScope.() -> Unit
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = BorderStroke(1.dp, CdsBorder)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(icono, contentDescription = null, tint = CdsNavy, modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = titulo,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = CdsText,
                    modifier = Modifier.weight(1f)
                )
                if (etiqueta != null) {
                    Text(
                        text = etiqueta,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = CdsTextMuted,
                        modifier = Modifier
                            .background(CdsField, RoundedCornerShape(8.dp))
                            .padding(horizontal = 8.dp, vertical = 3.dp)
                    )
                }
            }
            Spacer(modifier = Modifier.height(12.dp))
            content()
        }
    }
}

/** Chip de estado: verde (correcto) o rojo (alerta). */
@Composable
fun EstadoChip(texto: String, icono: ImageVector, alerta: Boolean = false) {
    val fondo = if (alerta) CdsAlertContainer else CdsGreenContainer
    val color = if (alerta) CdsAlert else CdsGreen
    Surface(shape = CircleShape, color = fondo) {
        Row(
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Icon(icono, contentDescription = null, tint = color, modifier = Modifier.size(13.dp))
            Text(text = texto, color = color, fontSize = 11.sp, fontWeight = FontWeight.Bold)
        }
    }
}

/** Colores de los campos de texto (fondo gris suave, borde navy al enfocar). */
@Composable
fun cdsFieldColors(): TextFieldColors = OutlinedTextFieldDefaults.colors(
    focusedBorderColor = CdsNavy,
    unfocusedBorderColor = Color.Transparent,
    focusedContainerColor = CdsField,
    unfocusedContainerColor = CdsField,
    focusedLeadingIconColor = CdsNavy,
    unfocusedLeadingIconColor = CdsTextMuted,
    focusedLabelColor = CdsNavy,
    cursorColor = CdsNavy
)
