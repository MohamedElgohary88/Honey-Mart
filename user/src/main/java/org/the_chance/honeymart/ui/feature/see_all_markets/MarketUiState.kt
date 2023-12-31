package org.the_chance.honeymart.ui.feature.see_all_markets

import org.the_chance.honeymart.domain.model.Market
import org.the_chance.honeymart.domain.util.ErrorHandler


data class MarketsUiState(
    val isLoading: Boolean = false,
    val isError: Boolean = false,
    val error: ErrorHandler? = null,
    val page: Int = 1,
    val markets: List<MarketUiState> = listOf(),
)

data class MarketUiState(
    val marketId: Long = 0L,
    val marketName: String = "",
    val marketImage: String = "",
    val isClicked: Boolean = false
)


fun Market.toMarketUiState(): MarketUiState {
    return MarketUiState(
        marketId = marketId,
        marketName = marketName,
        marketImage = imageUrl
    )
}

fun MarketsUiState.contentScreen() = !this.isError && this.markets.isNotEmpty()