package com.jsalinas.cruzdelsurapp.presentation.navigation

object RutasNav {
    const val LOGIN = "login"
    const val RECUPERAR = "recuperar"
    const val INICIO = "inicio"
    const val FORMULARIO = "form?id={id}"
    const val BUSCAR = "buscar"
    const val DETALLE = "detalle/{id}"

    fun getTitulo(ruta: String?): String{
        return when{
            ruta == INICIO -> "Inicio"
            ruta?.startsWith("form") == true -> "Registrar"
            ruta?.startsWith(BUSCAR) == true -> "Buscar"
            ruta?.startsWith("detalle") == true -> "Detalle"
            else -> "Inicio"
        }
    }
}
