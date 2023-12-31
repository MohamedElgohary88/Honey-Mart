package org.the_chance.honymart.ui.composables

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import org.the_chance.design_system.R
import org.the_chance.honymart.ui.composables.HoneyFilledButton
import org.the_chance.honymart.ui.theme.black60
import org.the_chance.honymart.ui.theme.dimens

@Composable
fun ConnectionErrorPlaceholder(state: Boolean, onClickTryAgain: () -> Unit) {
        AnimatedVisibility(
            visible = state,
            enter = fadeIn(
                animationSpec = tween(durationMillis = 500)
            ) + slideInVertically(),
            exit = fadeOut(
                animationSpec = tween(durationMillis = 500)
            ) + slideOutVertically()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(MaterialTheme.dimens.space16),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.no_connection_placeholder_img),
                    contentDescription = stringResource(R.string.network_error),
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight()
                )
                Text(
                    text = stringResource(R.string.oops_no_connection),
                    style = MaterialTheme.typography.bodyMedium,
                    color = black60,
                    textAlign = TextAlign.Center,
                )
                HoneyFilledButton(
                    label = stringResource(id = R.string.try_again_text),
                    onClick = onClickTryAgain,
                    modifier = Modifier
                        .padding(top = MaterialTheme.dimens.space16)
                        .wrapContentWidth(),
                )

            }
        }
}


@Preview
@Composable
fun PreviewConnectionErrorScaffold() {
    ConnectionErrorPlaceholder(true) {}
}