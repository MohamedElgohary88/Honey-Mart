package org.the_chance.honeymart.ui.feature.home

import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.update
import org.the_chance.honeymart.domain.model.Category
import org.the_chance.honeymart.domain.model.Coupon
import org.the_chance.honeymart.domain.model.Market
import org.the_chance.honeymart.domain.model.Order
import org.the_chance.honeymart.domain.model.Product
import org.the_chance.honeymart.domain.model.RecentProduct
import org.the_chance.honeymart.domain.usecase.GetAllCategoriesInMarketUseCase
import org.the_chance.honeymart.domain.usecase.GetAllMarketsUseCase
import org.the_chance.honeymart.domain.usecase.GetAllOrdersUseCase
import org.the_chance.honeymart.domain.usecase.GetAllProductsUseCase
import org.the_chance.honeymart.domain.usecase.GetRecentProductsUseCase
import org.the_chance.honeymart.domain.usecase.usecaseManager.user.UserCouponsManagerUseCase
import org.the_chance.honeymart.domain.util.ErrorHandler
import org.the_chance.honeymart.ui.base.BaseViewModel
import org.the_chance.honeymart.ui.feature.SeeAllmarkets.toMarketUiState
import org.the_chance.honeymart.ui.feature.coupons.toCouponUiState
import org.the_chance.honeymart.ui.feature.marketInfo.toCategoryUiState
import org.the_chance.honeymart.ui.feature.new_products.toRecentProductUiState
import org.the_chance.honeymart.ui.feature.orders.OrderStates
import org.the_chance.honeymart.ui.feature.orders.toOrderUiState
import org.the_chance.honeymart.ui.feature.product.toProductUiState
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getAllMarkets: GetAllMarketsUseCase,
    private val getAllCategoriesInMarket: GetAllCategoriesInMarketUseCase,
    private val getRecentProducts: GetRecentProductsUseCase,
    private val getAllDiscoverProducts: GetAllProductsUseCase,
    private val getAllOrders: GetAllOrdersUseCase,
    private val userCoupons: UserCouponsManagerUseCase,
) : BaseViewModel<HomeUiState, HomeUiEffect>(HomeUiState()), HomeInteractionListener {

    override val TAG: String = this::class.java.simpleName


    init {
        getData()
    }
    override fun getData() {
        _state.update {
            it.copy(isLoading = true, error = null, isConnectionError = false)
        }
        getAllMarkets()
        getUserCoupons()
        getRecentProducts()
        getDoneOrders()
        getDiscoverProducts()
    }

    /// region Markets
    private fun getAllMarkets() {
        tryToExecute(
            getAllMarkets::invoke,
            ::onGetMarketSuccess,
            ::onGetMarketError
        )
    }

    private fun onGetMarketSuccess(markets: List<Market>) {
        _state.update {
            it.copy(markets = markets.map { market -> market.toMarketUiState() })
        }

        if (markets.isNotEmpty()) {
            _state.update { it.copy(selectedMarketId = markets[0].marketId) }
            getMarketCategories(markets[0].marketId)
        }
    }

    private fun onGetMarketError(error: ErrorHandler) {
        _state.update {
            it.copy(
                isLoading = false,
                error = error,
                isConnectionError = error is ErrorHandler.NoConnection,
            )
        }
    }
    /// endregion

    /// region Categories
    private fun getMarketCategories(marketId: Long) {
        tryToExecute(
            { getAllCategoriesInMarket(marketId) },
            ::onGetAllCategoriesInMarketSuccess,
            ::onGetAllCategoriesInMarketError
        )
    }

    private fun onGetAllCategoriesInMarketSuccess(categories: List<Category>) {
        _state.update {
            it.copy(
                isLoading = false,
                isCategoryLoading = false,
                categories = categories.map { category -> category.toCategoryUiState() }
            )
        }
    }

    private fun onGetAllCategoriesInMarketError(error: ErrorHandler) {
        _state.update {
            it.copy(
                isLoading = false,
                error = error,
                isConnectionError = error is ErrorHandler.NoConnection,
            )
        }
    }
    /// endregion

    /// region Coupons
    private fun getUserCoupons() {
        tryToExecute(
            { userCoupons.getAllCouponsUseCase.getUserCoupons() },
            ::onGetCouponsSuccess,
            ::onGetUserCouponsError
        )
    }

    private fun onGetUserCouponsError(error: ErrorHandler) {
        _state.update {
            it.copy(
                isLoading = false,
                error = error,
                isConnectionError = error is ErrorHandler.NoConnection
            )
        }

        if (error is ErrorHandler.UnAuthorized)
            getAllValidCoupons()
    }

    private fun getAllValidCoupons() {
        tryToExecute(
            { userCoupons.getAllCouponsUseCase.getAllValidCoupons() },
            ::onGetCouponsSuccess,
            ::onGetAllValidCouponsError
        )
    }

    private fun onGetAllValidCouponsError(error: ErrorHandler) {
        _state.update {
            it.copy(
                isLoading = false,
                error = error,
                isConnectionError = error is ErrorHandler.NoConnection
            )
        }
    }

    private fun onGetCouponsSuccess(coupons: List<Coupon>) {
        _state.update {
            it.copy(
                isLoading = false,
                coupons = coupons.map { coupon -> coupon.toCouponUiState() }
            )
        }
    }
    /// endregion

    /// region Recent Products
    private fun getRecentProducts() {
        tryToExecute(
            getRecentProducts::invoke,
            ::onGetRecentProductsSuccess,
            ::onGetRecentProductsError,
        )
    }

    private fun onGetRecentProductsSuccess(products: List<RecentProduct>) {
        _state.update {
            it.copy(
                recentProducts = products.map { product -> product.toRecentProductUiState() }
            )
        }
    }

    private fun onGetRecentProductsError(error: ErrorHandler) {
        _state.update {
            it.copy(
                isLoading = false,
                error = error,
                isConnectionError = error is ErrorHandler.NoConnection
            )
        }
    }
    /// endregion

    /// region Done Products

    private fun getDoneOrders() {
        tryToExecute(
            { getAllOrders(OrderStates.DONE.state) },
            ::onGetDoneOrdersSuccess,
            ::onGetDoneOrdersError
        )
    }

    private fun onGetDoneOrdersSuccess(orders: List<Order>) {
        _state.update {
            it.copy(
                lastPurchases = orders.map { order -> order.toOrderUiState() })
        }
    }

    private fun onGetDoneOrdersError(error: ErrorHandler) {
        _state.update {
            it.copy(
                isLoading = false,
                error = error,
                isConnectionError = error is ErrorHandler.NoConnection
            )
        }
    }
    /// endregion

    /// region Discover Products

    private fun getDiscoverProducts() {
        tryToExecute(
            getAllDiscoverProducts::invoke,
            ::onGetDiscoverProductsSuccess,
            ::onGetDiscoverProductsError,
        )
    }

    private fun onGetDiscoverProductsSuccess(products: List<Product>) {
        _state.update {
            it.copy(
                discoverProducts = products.map { product -> product.toProductUiState() }
            )
        }
    }

    private fun onGetDiscoverProductsError(error: ErrorHandler) {
        _state.update {
            it.copy(
                isLoading = false,
                error = error,
                isConnectionError = error is ErrorHandler.NoConnection
            )
        }
    }
    /// endregion


  
    /// region Interactions

    override fun onClickCategory(categoryId: Long, position: Int) {
        effectActionExecutor(
            _effect,
            HomeUiEffect.NavigateToProductScreenEffect(
                categoryId,
                state.value.selectedMarketId,
                position
            )
        )
    }

    override fun onClickPagerItem(marketId: Long) {
        effectActionExecutor(_effect, HomeUiEffect.NavigateToMarketScreenEffect(marketId))
    }

    override fun onClickSeeAllMarkets() {
        effectActionExecutor(_effect, HomeUiEffect.NavigateToSeeAllMarketEffect)
    }

    override fun onClickGetCoupon(couponId: Long) {
        _state.update { it.copy(isLoading = true) }
        tryToExecute(
            { userCoupons.clipCouponUseCase(couponId) },
            { onClipCouponSuccess() },
            ::onClipCouponError
        )
    }

    private fun onClipCouponSuccess() {
        getUserCoupons()
    }

    private fun onClipCouponError(errorHandler: ErrorHandler) {
        _state.update {
            it.copy(
                isLoading = false,
                error = errorHandler,
                isConnectionError = errorHandler is ErrorHandler.NoConnection,
            )
        }

        if (errorHandler is ErrorHandler.UnAuthorized)
            effectActionExecutor(_effect, HomeUiEffect.UnAuthorizedUserEffect)
    }

    override fun onClickProductItem(productId: Long) {
        effectActionExecutor(_effect, HomeUiEffect.NavigateToProductsDetailsScreenEffect(productId))
    }

    override fun onClickLastPurchases(orderId: Long) {
        effectActionExecutor(_effect, HomeUiEffect.NavigateToOrderDetailsScreenEffect(orderId))
    }
    override fun onClickSearchBar() {
        effectActionExecutor(_effect, HomeUiEffect.NavigateToSearchScreenEffect)
    }

    override fun onClickChipCategory(marketId: Long) {
        _state.update { it.copy(selectedMarketId = marketId, isCategoryLoading = true) }
        getMarketCategories(marketId)
    }

    override fun onClickSeeAllNewProducts() {
        effectActionExecutor(_effect, HomeUiEffect.NavigateToNewProductsScreenEffect)
    }

    override fun onClickLastPurchasesSeeAll() {
        effectActionExecutor(_effect, HomeUiEffect.NavigateToOrderScreenEffect)
    }

    /// endregion
}


