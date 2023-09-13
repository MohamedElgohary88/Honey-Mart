package org.the_chance.honeymart.ui.feature.marketInfo

import androidx.lifecycle.SavedStateHandle
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.update
import org.the_chance.honeymart.domain.model.MarketDetails
import org.the_chance.honeymart.domain.usecase.GetMarketDetailsUseCase
import org.the_chance.honeymart.domain.util.ErrorHandler
import org.the_chance.honeymart.ui.base.BaseViewModel
import javax.inject.Inject

@HiltViewModel
class MarketViewModel @Inject constructor(
    private val getMarketDetails: GetMarketDetailsUseCase,
    savedStateHandle: SavedStateHandle,
) : BaseViewModel<MarketDetailsUiState, MarketUiEffect>(MarketDetailsUiState()),
    MarketInteractionListener {

    private val categoryArgs: CategoryArgs = CategoryArgs(savedStateHandle)

    override val TAG: String = this::class.java.simpleName

    init {
        onGetData()
    }

    override fun onGetData() {
        getAllCategory()
    }

    override fun onClickCategory(categoryId: Long, position: Int) {
        effectActionExecutor(
            _effect,
            MarketUiEffect.ClickMarketEffect(
                categoryId,
                categoryArgs.marketId.toLong(),
                position
            )
        )
    }

    private fun getAllCategory() {
        _state.update { it.copy(isLoading = true, isError = false) }
        tryToExecute(
            { getMarketDetails(categoryArgs.marketId.toLong()) },
            ::onGetCategorySuccess,
            ::onGetCategoryError
        )
    }

    private fun onGetCategorySuccess(market: MarketDetails) {
        this._state.update {
            it.copy(
                isLoading = false,
                error = null,
                categories = market.categories.map {category -> category.toCategoryUiState() },
                categoriesCount = market.categoriesCount,
                productsCount = market.productsCount,
                imageUrl = market.imageUrl,
                marketName = market.marketName,
                address = market.address
            )
        }
    }

    private fun onGetCategoryError(error: ErrorHandler) {
        _state.update { it.copy(isLoading = false, error = error) }
        if (error is ErrorHandler.NoConnection) {
            _state.update { it.copy(isError = true) }
        }
    }
}