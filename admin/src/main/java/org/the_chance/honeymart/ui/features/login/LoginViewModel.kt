package org.the_chance.honeymart.ui.features.login

import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.update
import org.the_chance.honeymart.domain.usecase.usecaseManager.admin.AdminAuthManagerUseCase
import org.the_chance.honeymart.domain.util.ErrorHandler
import org.the_chance.honeymart.domain.util.ValidationState
import org.the_chance.honeymart.ui.base.BaseViewModel
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val adminAuthManager: AdminAuthManagerUseCase
) : BaseViewModel<LoginUiState, LoginUiEffect>(LoginUiState()),
    LoginInteractionListener {

    override val TAG: String = this::class.java.simpleName

    init {
        checkAuthorization()
    }

    private fun checkAuthorization() {
        _state.update { it.copy(isLoading = true) }
        tryToExecute(
            { adminAuthManager.checkAuthentication() },
            { onAuthorizationSuccess() },
            ::onAuthorizationError,
        )
    }

    private fun onAuthorizationSuccess() {
        effectActionExecutor(_effect, LoginUiEffect.ClickLoginEffect)
    }

    private fun onAuthorizationError(error: ErrorHandler) {
        _state.update { it.copy(isLoading = false, error = error) }
    }

    private fun loginAdmin(email: String, password: String) {
        _state.update { it.copy(isAuthenticating = true) }
        tryToExecute(
            { adminAuthManager.login(email, password) },
            { onLoginSuccess() },
            ::onLoginError,
        )
    }

    private fun onLoginSuccess() {
        _state.update { it.copy(isAuthenticating = false) }
        effectActionExecutor(_effect, LoginUiEffect.ClickLoginEffect)
    }

    private fun onLoginError(error: ErrorHandler) {
        _state.update { it.copy(isAuthenticating = false, error = error) }
        effectActionExecutor(_effect, LoginUiEffect.ShowInvalidDetailsToastEffect)
    }

    override fun onClickLogin() {
        val isFieldsNotEmpty = state.value.email.isEmpty && state.value.password.isEmpty
        if (isFieldsNotEmpty) {
            loginAdmin(email = state.value.email.value, password = state.value.password.value)
        } else {
            effectActionExecutor(_effect, LoginUiEffect.ShowEmptyFieldsToastEffect)
        }
    }

    override fun onEmailInputChange(email: CharSequence) {
        val emailState = adminAuthManager.validationLogin.validateEmail(email.trim().toString())
        _state.update {
            it.copy(
                email = FieldState(
                    error = when (emailState) {
                        ValidationState.BLANK_EMAIL -> "email shouldn't be empty"
                        ValidationState.INVALID_EMAIL -> "Invalid email"
                        else -> ""
                    },
                    value = email.toString(),
                    isEmpty = emailState == ValidationState.VALID_EMAIL
                ),
            )
        }
    }

    override fun onPasswordInputChanged(password: CharSequence) {
        val passwordState = adminAuthManager.validationLogin.validatePassword(password.toString())
        _state.update {
            it.copy(
                password = FieldState(
                    error = when (passwordState) {
                        ValidationState.BLANK_PASSWORD -> "Password shouldn't be empty"
                        ValidationState.INVALID_PASSWORD_LENGTH_SHORT ->
                            "Password length must be at least 4"

                        else -> ""
                    },
                    value = password.toString(),
                    isEmpty = passwordState == ValidationState.VALID_PASSWORD
                ),
            )
        }
    }
}