package org.the_chance.honeymart.ui.feature.uistate

/**
 * Created by Aziza Helmy on 6/16/2023.
 */

data class LoginUiState(
    val isLoading: Boolean = true,
    val error: Int = 0,
    val email: String = "",
    val password: String = ""
)