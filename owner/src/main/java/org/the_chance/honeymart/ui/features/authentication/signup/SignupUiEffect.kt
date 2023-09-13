package org.the_chance.honeymart.ui.features.authentication.signup

import org.the_chance.honeymart.ui.base.BaseUiEffect

sealed class SignupUiEffect : BaseUiEffect {
    object ClickLoginEffect : SignupUiEffect()
    object ShowValidationToast: SignupUiEffect()
    object NavigateToCategoriesEffect: SignupUiEffect()
    object NavigateToWaitingApproveEffect: SignupUiEffect()
    object ClickLogoutEffect: SignupUiEffect()
}