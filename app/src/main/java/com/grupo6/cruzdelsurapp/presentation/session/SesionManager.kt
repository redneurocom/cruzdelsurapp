package com.grupo6.cruzdelsurapp.presentation.session

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.grupo6.cruzdelsurapp.domain.model.Usuario
import javax.inject.Inject
import javax.inject.Singleton

// Sesión del operario en memoria; se pierde al cerrar la app y se vuelve a pedir el login.
@Singleton
class SesionManager @Inject constructor() {
    var usuario: Usuario? by mutableStateOf(null)
        private set

    val token: String? get() = usuario?.token

    fun iniciarSesion(usuario: Usuario) {
        this.usuario = usuario
    }

    fun cerrarSesion() {
        usuario = null
    }
}
