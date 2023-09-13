package org.the_chance.honymart.ui.composables


import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Snackbar
import androidx.compose.material.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.constraintlayout.compose.ConstraintLayout
import org.the_chance.honymart.ui.theme.Typography
import org.the_chance.honymart.ui.theme.dimens

@Composable
fun SnackBarWithDuration(
    message: String,
    undoAction: () -> Unit,
    onDismiss: () -> Unit,
    text: String = "Undo",
) {
    val snackbarHostState = remember { SnackbarHostState() }
    ConstraintLayout(
        modifier = Modifier.fillMaxSize()
    ) {
        val snackBar = createRef()
        LaunchedEffect(key1 = snackbarHostState) {
            val snackBarResult = snackbarHostState.showSnackbar(
                message = message,
                actionLabel = "Close",
                duration = SnackbarDuration.Short,
            )
            when (snackBarResult) {
                SnackbarResult.Dismissed -> onDismiss()
                SnackbarResult.ActionPerformed -> onDismiss()
            }

        }

        SnackbarHost(
            hostState = snackbarHostState,
            modifier = Modifier.constrainAs(snackBar) {
                bottom.linkTo(parent.bottom)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
            }
        ) {
            Snackbar(
                modifier = Modifier.padding(horizontal = MaterialTheme.dimens.space16),
                action = {
                    Text(
                        text,
                        modifier = Modifier.clickable(onClick = {
                            undoAction()
                            snackbarHostState.currentSnackbarData?.dismiss()
                        }),
                        style = Typography.displayLarge,
                        color = MaterialTheme.colorScheme.primary
                    )
                },
            ) {
                Text(
                    text = message,
                    style = Typography.bodySmall,
                )
            }
        }
    }
}