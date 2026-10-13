package com.grupo6.cruzdelsurapp.presentation.navigation

object RutasNav {
    const val LOGIN = "login"
    const val INICIO = "inicio"
    const val FORMULARIO = "form?id={id}"
    const val BUSCAR = "buscar"
    const val DETALLE = "detalle/{id}"

    fun getTitulo(ruta: String?): String{
        return when{
            ruta == LOGIN -> "Inicio de Sesión"
            ruta == INICIO -> "Manifiesto de Bodega"
            ruta?.startsWith("form") == true -> "Registrar Equipaje"
            ruta?.startsWith(BUSCAR) == true -> "Consulta de Equipaje"
            ruta?.startsWith("detalle") == true -> "Detalle de Equipaje"
            else -> "Mi Equipaje"
        }
    }
}