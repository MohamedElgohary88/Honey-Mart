package org.the_chance.honeymart.ui.composables

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import org.the_chance.design_system.R
import org.the_chance.honymart.ui.theme.HoneyMartTheme
import org.the_chance.honymart.ui.theme.dimens

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun EmptyProductPlaceholder(state: Boolean){
    HoneyMartTheme {
        AnimatedVisibility(
            visible = state,
            enter = fadeIn(
                animationSpec = tween(durationMillis = 500)) + slideInVertically() + scaleIn(),
            exit = fadeOut(
                animationSpec = tween(durationMillis = 500)) + slideOutVertically() + scaleOut()
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.placeholder_wish_list),
                    contentDescription = stringResource(R.string.empty_product),
                    contentScale = ContentScale.Crop
                )
                Text(
                    modifier = Modifier.padding(top = MaterialTheme.dimens.space56),
                    text = stringResource(R.string.there_is_no_product_here),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onBackground,
                    textAlign = TextAlign.Center
                )
            }
        }

    }
}

@Preview
@Composable
fun PreviewEmptyProductScaffold(){
    EmptyProductPlaceholder(true)
}