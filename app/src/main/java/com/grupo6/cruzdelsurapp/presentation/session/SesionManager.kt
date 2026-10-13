package com.grupo6.cruzdelsurapp.presentation.session

import com.grupo6.cruzdelsurapp.domain.model.Usuario
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Sesión del operario en memoria (igual que UiEventBus, es un singleton de Hilt).
 * Al cerrar la app la sesión se pierde y se vuelve a pedir el login.
 */
@Singleton
class SesionManager @Inject constructor() {
    private val _usuario = MutableStateFlow<Usuario?>(null)
    val usuario: StateFlow<Usuario?> = _usuario.asStateFlow()

    /** Token para el encabezado Authorization (null si no hay sesión). */
    val token: String? get() = _usuario.value?.token

    fun iniciarSesion(usuario: Usuario) {
        _usuario.value = usuario
    }

    fun cerrarSesion() {
        _usuario.value = null
    }
}
