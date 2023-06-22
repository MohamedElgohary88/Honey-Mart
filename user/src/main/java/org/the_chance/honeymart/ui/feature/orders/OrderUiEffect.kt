package org.the_chance.honeymart.ui.feature.orders


sealed class OrderUiEffect {
    data class ClickOrderEffect(val orderId: Int) : OrderUiEffect()
    object UnAuthorizedUserEffect : OrderUiEffect()
    object ClickDiscoverMarketsEffect : OrderUiEffect()
}