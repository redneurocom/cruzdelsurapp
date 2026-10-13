package com.grupo6.cruzdelsurapp.presentation.event

import com.grupo6.cruzdelsurapp.presentation.common.SnackbarType

interface UiEvent {
    data class ShowSnackbar(val message: String, val type: SnackbarType) : UiEvent
}