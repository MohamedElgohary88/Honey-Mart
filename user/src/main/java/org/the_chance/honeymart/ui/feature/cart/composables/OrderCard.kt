package org.the_chance.honeymart.ui.feature.cart.composables


import android.icu.text.DecimalFormat
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.hilt.navigation.compose.hiltViewModel
import org.the_chance.honeymart.ui.feature.cart.CartViewModel
import org.the_chance.honymart.ui.composables.CustomButton
import org.the_chance.honymart.ui.theme.black16
import org.the_chance.honymart.ui.theme.black60
import org.the_chance.honymart.ui.theme.primary100
import org.the_chance.user.R

@Composable
fun CartCardView(
    modifier: Modifier = Modifier,
    totalPrice: String = "300,000 $",
    isLoading: Boolean ,
    viewModel: CartViewModel = hiltViewModel()
) {
    Card(
        shape = RoundedCornerShape(topEnd = 16.dp, topStart = 16.dp, bottomEnd = 0.dp, bottomStart = 0.dp),
    modifier = Modifier
        .fillMaxWidth()
        .height(100.dp)
        ,
        colors =CardDefaults.cardColors(
            containerColor = Color.White,
        )
    ) {
        ConstraintLayout(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp)
        ) {
            val (priceInDollars, orderNowButton, TotalPrice) = createRefs()
            Text(
                text = formatCurrencyWithNearestFraction(totalPrice.toDouble()),
                color = black60,
                style = org.the_chance.honymart.ui.theme.Typography.bodyMedium,
                modifier = Modifier.constrainAs(priceInDollars) {
                    top.linkTo(parent.top, margin = 8.dp)
                    end.linkTo(parent.end)
                }
            )

            CustomButton(
                onClick = { viewModel.onClickOrderNowButton() },
                labelIdStringRes = org.the_chance.design_system.R.string.order_now,
                isEnable = !isLoading,
                idIconDrawableRes = org.the_chance.design_system.R.drawable.icon_cart,
                modifier = Modifier
                    .fillMaxWidth()
                    .constrainAs(orderNowButton) {
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                        bottom.linkTo(parent.bottom, margin = 16.dp)
                    },
                background = primary100,
            )
            Text(
                text = stringResource(R.string.total_price),
                color = black16,
                style = org.the_chance.honymart.ui.theme.Typography.displaySmall,
                modifier = Modifier.constrainAs(TotalPrice) {
                    start.linkTo(parent.start)
                    top.linkTo(parent.top, margin = 8.dp)
                    bottom.linkTo(orderNowButton.top, margin = 8.dp)
                }
            )
        }
    }
}


fun formatCurrencyWithNearestFraction(amount: Double):String {
    val decimalFormat = DecimalFormat("#,##0.0'$'")
    return decimalFormat.format(amount)
}


