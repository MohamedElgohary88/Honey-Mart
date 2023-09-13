package org.the_chance.honeymart.ui.features.authentication.signup

interface SignupInteractionListener {
    fun onClickLogin()
    fun onClickContinue()
    fun onFullNameInputChange(fullName: CharSequence)
    fun onEmailInputChange(email: CharSequence)
    fun onPasswordInputChanged(password: CharSequence)
    fun onConfirmPasswordChanged(confirmPassword: CharSequence)
}