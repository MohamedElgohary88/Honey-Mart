package org.the_chance.honeymart.ui.features.profile.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.the_chance.design_system.R
import org.the_chance.honeymart.ui.features.profile.MarketStatus
import org.the_chance.honymart.ui.theme.HoneyMartTheme
import org.the_chance.honymart.ui.theme.success
import org.the_chance.honymart.ui.theme.error

@Composable
fun StatusChip(
    status: MarketStatus,
    onClickChangeStatus: () -> Unit,
    modifier: Modifier = Modifier
) {
    val statusColor by animateColorAsState(
        targetValue = if (status == MarketStatus.ONLINE) success else error,
        label = "Status color"
    )

    Row(
        modifier = modifier
            .wrapContentSize()
            .background(
                color = MaterialTheme.colorScheme.background,
                shape = MaterialTheme.shapes.small
            )
            .clickable { onClickChangeStatus() }
            .padding(8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Icon(
            painter = if (status == MarketStatus.ONLINE) painterResource(id = R.drawable.icon_online_market)
            else painterResource(id = R.drawable.icon_offline_market),
            contentDescription = stringResource(R.string.icon_market_status),
            tint = statusColor
        )
        Text(
            text = if (status == MarketStatus.ONLINE) stringResource(R.string.online)
            else stringResource(R.string.offline),
            style = MaterialTheme.typography.displayLarge,
            color = statusColor
        )
    }
}

@Preview
@Composable
fun PreviewStatusChip() {
    HoneyMartTheme {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            StatusChip(status = MarketStatus.ONLINE, onClickChangeStatus = {})
            StatusChip(status = MarketStatus.OFFLINE, onClickChangeStatus = {})
        }
    }
}