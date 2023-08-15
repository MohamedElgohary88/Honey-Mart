package org.the_chance.honeymart.ui.features.login

import org.the_chance.honeymart.domain.util.ErrorHandler
import org.the_chance.honeymart.ui.features.signup.FieldState
import org.the_chance.honeymart.ui.features.signup.ValidationToast

data class LoginUiState(
    val isLoading: Boolean = false,
    val error: ErrorHandler? = null,
    val isLogin: Boolean = false,

    val emailState: FieldState = FieldState(),
    val passwordState: FieldState = FieldState(),
    val validationToast: ValidationToast = ValidationToast()
)
