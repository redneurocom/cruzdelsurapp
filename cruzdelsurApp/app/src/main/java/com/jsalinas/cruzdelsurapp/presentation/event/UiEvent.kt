package com.jsalinas.cruzdelsurapp.presentation.event

import com.jsalinas.cruzdelsurapp.presentation.common.SnackbarType

interface UiEvent {
    data class ShowSnackbar(val message: String, val type: SnackbarType) : UiEvent
}