package org.the_chance.honeymart.ui.feature.home

import android.icu.text.DecimalFormat
import org.the_chance.honeymart.domain.model.CouponEntity
import org.the_chance.honeymart.domain.util.ErrorHandler
import org.the_chance.honeymart.ui.feature.category.CategoryUiState
import org.the_chance.honeymart.ui.feature.coupons.CouponUiState
import org.the_chance.honeymart.ui.feature.markets.MarketUiState
import org.the_chance.honeymart.ui.feature.new_products.RecentProductUiState
import org.the_chance.honeymart.ui.feature.orders.OrderUiState
import org.the_chance.honeymart.ui.feature.product.ProductUiState
import org.the_chance.honeymart.ui.feature.product.toProductUiState

data class HomeUiState(
    val isLoading: Boolean = true,
    val isConnectionError: Boolean = false,
    val error: ErrorHandler? = null,
    val selectedMarketId: Long = 0L,
    val markets: List<MarketUiState> = emptyList(),
    val categories: List<CategoryUiState> = emptyList(),
    val coupons: List<CouponUiState> = emptyList(),
    val recentProducts: List<RecentProductUiState> = emptyList(),
    val lastPurchases: List<OrderUiState> = emptyList(),
    val discoverProducts: List<ProductUiState> = emptyList(),
){
    val shuffledMarket = if (markets.size > 3 ) markets.shuffled().take(3) else markets
}





fun CouponEntity.toCouponUiState() = CouponUiState(
    couponId = couponId,
    count = count,
    discountPrice = product.productPrice.discountedPrice(discountPercentage = discountPercentage),
    expirationDate = expirationDate.formatDate(),
    product = product.toProductUiState(),
    isClipped = isClipped,
)


fun HomeUiState.showHome() = markets.isNotEmpty() && !isConnectionError

fun String.formatDate(): String {
    val date = this
    val year = date.substring(0, 4)
    val month = date.substring(5, 7)
    val day = date.substring(8, 10)
    return "$day.$month.$year"
}

fun Double.formatCurrencyWithNearestFraction(): String {
    val decimalFormat = DecimalFormat("'$'#,##0.0")
    return decimalFormat.format(this)
}

fun Double.discountedPrice(discountPercentage: Double): Double {
    return this - (this * discountPercentage / 100)
}
