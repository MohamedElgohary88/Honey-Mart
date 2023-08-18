package org.the_chance.honeymart.ui.features.signup

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import org.the_chance.design_system.R
import org.the_chance.honeymart.LocalNavigationProvider
import org.the_chance.honeymart.ui.composables.HoneyAuthScaffold
import org.the_chance.honeymart.ui.features.login.navigateToLogin
import org.the_chance.honeymart.ui.features.signup.market_info.navigateToMarketInfoScreen
import org.the_chance.honymart.ui.composables.HoneyAuthFooter
import org.the_chance.honymart.ui.composables.HoneyAuthHeader
import org.the_chance.honymart.ui.composables.HoneyFilledButton
import org.the_chance.honymart.ui.composables.HoneyTextField
import org.the_chance.honymart.ui.composables.HoneyTextFieldPassword
import org.the_chance.honymart.ui.theme.primary100

@Composable
fun SignupScreen(
    viewModel: SignUpViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()
    val context = LocalContext.current
    val navController = LocalNavigationProvider.current
    LaunchedEffect(key1 = true) {
        viewModel.effect.collect {
            when (it) {
                SignupUiEffect.ShowValidationToast -> {
                    Toast.makeText(
                        context,
                        state.validationToast.message,
                        Toast.LENGTH_LONG
                    ).show()
                }

                SignupUiEffect.ClickContinueEffect -> {
                    navController.navigateToMarketInfoScreen()
                }

                SignupUiEffect.ClickLoginEffect -> {
                    navController.navigateToLogin()
                }
            }
        }
    }
    SignupContent(listener = viewModel, state = state)
}

@Composable
fun SignupContent(
    state: SignupUiState,
    listener: SignupInteractionListener,
) {
    HoneyAuthScaffold(
        modifier = Modifier.imePadding()
    ) {
        HoneyAuthHeader(
            title = stringResource(R.string.sign_up),
            subTitle = stringResource(R.string.create_an_account_name_your_market),
        )

        Column {
            HoneyTextField(
                text = state.fullNameState.value,
                hint = stringResource(R.string.full_name),
                iconPainter = painterResource(R.drawable.ic_person),
                onValueChange = listener::onFullNameInputChange,
                errorMessage = state.fullNameState.errorState,
            )
            HoneyTextField(
                text = state.emailState.value,
                hint = stringResource(R.string.email),
                iconPainter = painterResource(R.drawable.ic_email),
                onValueChange = listener::onEmailInputChange,
                errorMessage = state.emailState.errorState,
            )
            HoneyTextFieldPassword(
                text = state.passwordState.value,
                hint = stringResource(R.string.password),
                iconPainter = painterResource(R.drawable.ic_password),
                onValueChange = listener::onPasswordInputChanged,
                errorMessage = state.passwordState.errorState,
            )
            HoneyTextFieldPassword(
                text = state.confirmPasswordState.value,
                hint = stringResource(R.string.confirm_password),
                iconPainter = painterResource(R.drawable.ic_password),
                onValueChange = listener::onConfirmPasswordChanged,
                errorMessage = state.confirmPasswordState.errorState,
                keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
            )
        }

        Column(
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            HoneyFilledButton(
                label = stringResource(R.string.continue_word),
                onClick = listener::onClickContinue,
                background = primary100,
                contentColor = Color.White,
                isLoading = state.isLoading
            )

            HoneyAuthFooter(
                text = stringResource(R.string.already_have_account),
                textButtonText = stringResource(R.string.log_in),
                onTextButtonClicked = listener::onClickLogin,
                modifier = Modifier.Companion.align(Alignment.CenterHorizontally)
            )
        }
    }

}


@Preview(name = "Tablet", device = Devices.TABLET, showSystemUi = true)
@Composable
fun PreviewSignupScreen() {
    SignupScreen()
}