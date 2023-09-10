package org.the_chance.honeymart.ui.feature.product.composable

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import org.the_chance.design_system.R
import org.the_chance.honymart.ui.theme.Shapes
import org.the_chance.honymart.ui.theme.dimens

@Composable
fun CategoryItem(
    iconPainter: Painter,
    categoryName: String,
    isSelected: Boolean,
    enable: Boolean,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {},
) {

    Column(
        modifier = Modifier.width(MaterialTheme.dimens.space56),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.space4)
    ) {
        IconButton(
            modifier = modifier
                .size(MaterialTheme.dimens.space56)
                .background(
                    if (isSelected) MaterialTheme.colorScheme.primary
                    else MaterialTheme.colorScheme.secondaryContainer,
                    shape = Shapes.medium
                )
                .padding(bottom = MaterialTheme.dimens.space4),
            onClick = onClick,
            enabled = enable,
        ) {
            Icon(
                painter = iconPainter,
                contentDescription = stringResource(R.string.icon),
                tint = if (isSelected) White else MaterialTheme.colorScheme.onSecondaryContainer
            )
        }
        Text(
            text = categoryName,
            style = MaterialTheme.typography.displaySmall,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            color =
            if (isSelected) MaterialTheme.colorScheme.primary
            else MaterialTheme.colorScheme.onSecondaryContainer,
            textAlign = TextAlign.Start
        )
    }
}

