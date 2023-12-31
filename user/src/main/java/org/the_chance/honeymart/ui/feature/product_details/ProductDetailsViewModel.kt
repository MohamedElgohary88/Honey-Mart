package org.the_chance.honeymart.ui.feature.product_details

import androidx.lifecycle.SavedStateHandle
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.update
import org.the_chance.honeymart.domain.model.Product
import org.the_chance.honeymart.domain.usecase.usecaseManager.user.CartProductsManagerUseCase
import org.the_chance.honeymart.domain.usecase.usecaseManager.user.UserProductManagerUseCase
import org.the_chance.honeymart.domain.usecase.usecaseManager.user.UserWishListManagerUseCase
import org.the_chance.honeymart.domain.util.ErrorHandler
import org.the_chance.honeymart.ui.base.BaseViewModel
import org.the_chance.honeymart.ui.feature.product.toProductUiState
import javax.inject.Inject

@HiltViewModel
class ProductDetailsViewModel @Inject constructor(
    private val cartOperations: CartProductsManagerUseCase,
    private val productsOperations: UserProductManagerUseCase,
    private val wishListOperations: UserWishListManagerUseCase,
    savedStateHandle: SavedStateHandle,
) : BaseViewModel<ProductDetailsUiState, ProductDetailsUiEffect>(ProductDetailsUiState()),
    ProductDetailsInteraction {

    override val TAG: String = this::class.simpleName.toString()

    private val args = ProductDetailsArgs(savedStateHandle)


    init {
        getData()
    }

    fun getData() {
        getProductDetails(args.productId.toLong())
    }

    override fun confirmDeleteLastCartAndAddProductToNewCart(productId: Long, count: Int) {
        _state.update { it.copy(error = null, isConnectionError = false) }
        tryToExecute(
            { cartOperations.deleteAllCartUseCase() },
            { onDeleteCartSuccess(it, productId, count) },
            ::onDeleteCartError,
        )
    }

    override fun showDialog(productId: Long, count: Int) {
        _state.update {
            it.copy(
                dialogState = it.dialogState.copy(
                    showDialog = true, productId = productId, count = count
                )
            )
        }
    }

    override fun resetDialogState() {
        _state.update { it.copy(dialogState = it.dialogState.copy(showDialog = false)) }
    }

    private fun onDeleteCartSuccess(message: String, productId: Long, count: Int) {
        addProductToCart(productId, count)
    }

    private fun onDeleteCartError(error: ErrorHandler) {
        _state.update { it.copy(isLoading = false) }
        if (error is ErrorHandler.NoConnection) {
            _state.update { it.copy(isLoading = false, isConnectionError = true) }
        }
    }

    // region Product
    private fun getProductDetails(productId: Long) {
        _state.update { it.copy(isLoading = true, error = null, isConnectionError = false) }
        tryToExecute(
            { productsOperations.getProductDetailsUseCase(productId) },
            ::onGetProductSuccess,
            ::onGetProductError,
        )
    }

    private fun onGetProductSuccess(product: Product) {
        val reviewStatistic = product.reviewStatistic.toReviewStatisticUiState()
        val reviewsList = product.reviews.map { it.toReviewUiState() }
        _state.update {
            it.copy(
                error = null,
                isConnectionError = false,
                product = product.toProductUiState(),
                reviews = reviewsList,
                reviewStatisticUiState = reviewStatistic,
                isLoading = false
            )
        }
        _state.update {
            it.copy(
                image = it.product.productImages.first(),
                smallImages = it.product.productImages.drop(1),
                totalPrice = it.product.productPrice
            )
        }
        checkIfProductInWishList(args.productId.toLong())
    }

    private fun onGetProductError(error: ErrorHandler) {
        _state.update { it.copy(isLoading = false) }
        if (error is ErrorHandler.NoConnection) {
            _state.update { it.copy(isLoading = false, isConnectionError = true) }
        }
    }

    override fun onClickSmallImage(url: String) {
        val newList = mutableListOf<String>()
        newList.addAll(_state.value.smallImages.filter { it != url })
        newList.add(0, _state.value.image)
        _state.update { it.copy(smallImages = newList, image = url) }
    }

    override fun onClickBack() {
        effectActionExecutor(_effect, ProductDetailsUiEffect.OnBackClickEffect)
    }

    override fun increaseProductCount() {
        val currentQuantity = _state.value.quantity
        val newQuantity = if (currentQuantity >= 100) 100 else currentQuantity + 1
        _state.update { it.copy(quantity = newQuantity) }
        changePriceOnQuantityChange()
    }

    override fun decreaseProductCount() {
        val currentQuantity = _state.value.quantity
        val newQuantity = if (currentQuantity > 1) currentQuantity - 1 else 1
        _state.update { it.copy(quantity = newQuantity) }
        changePriceOnQuantityChange()
    }

    private fun changePriceOnQuantityChange() {
        _state.update {
            it.copy(totalPrice = it.product.productPrice * it.quantity)
        }
    }


    // endregion

    // region Cart

    override fun addProductToCart(productId: Long, count: Int) {
        _state.update {
            it.copy(
                isAddToCartLoading = true,
                error = null,
                isConnectionError = false,
            )
        }
        tryToExecute(
            { cartOperations.cartUseCase.addToCart(productId, count) },
            ::onAddProductToCartSuccess,
            { onAddProductToCartError(it, productId, count) }
        )
        _state.update { it.copy(snackBar = it.snackBar.copy(productId = productId)) }
    }

    private fun onAddProductToCartSuccess(message: String) {
        _state.update { it.copy(isAddToCartLoading = false) }
        effectActionExecutor(_effect, ProductDetailsUiEffect.AddToCartSuccess(message))
    }

    private fun onAddProductToCartError(error: ErrorHandler, productId: Long, count: Int) {
        _state.update { it.copy(isLoading = false, isAddToCartLoading = false) }

        when (error) {
            is ErrorHandler.NoConnection -> {
                _state.update { it.copy(isConnectionError = true) }
            }

            is ErrorHandler.UnAuthorized -> {
                effectActionExecutor(_effect, ProductDetailsUiEffect.UnAuthorizedUserEffect)
            }

            is ErrorHandler.ProductNotInSameCartMarket -> {
                effectActionExecutor(
                    _effect,
                    ProductDetailsUiEffect.ProductNotInSameCartMarketExceptionEffect(
                        productId,
                        count
                    )
                )
            }

            else -> {}
        }
    }
    // endregion

    // region Wishlist
    // region Check If Product In Wishlist

    private fun checkIfProductInWishList(productId: Long) {
        _state.update { it.copy(error = null, isConnectionError = false) }
        tryToExecute(
            { wishListOperations.getIfProductInWishListUseCase(productId) },
            ::onGetIfProductInWishListSuccess,
            ::onGetIfProductInWishListError
        )
    }

    private fun onGetIfProductInWishListSuccess(isFavorite: Boolean) {
        _state.update { it.copy(isLoading = false) }
        updateFavoriteState(isFavorite)
    }

    private fun onGetIfProductInWishListError(error: ErrorHandler) {
        _state.update { it.copy(isLoading = false) }
        if (error is ErrorHandler.NoConnection) {
            _state.update { it.copy(isLoading = false, isConnectionError = true) }
        }
    }


    // endregion

    // region Add Product To Wishlist

    private fun addProductToWishList(productId: Long) {
        _state.update { it.copy(error = null, isConnectionError = false) }
        tryToExecute(
            { wishListOperations.operationWishListUseCase.addToWishList(productId) },
            ::onAddProductToWishListSuccess,
            { error -> onAddProductToWishListError(error, productId) }
        )
    }

    private fun onAddProductToWishListSuccess(message: String) {
        _state.update {
            it.copy(isLoading = false)
        }
    }

    private fun onAddProductToWishListError(error: ErrorHandler, productId: Long) {
        _state.update { it.copy(isLoading = false, error = error) }
        when (error) {
            is ErrorHandler.NoConnection -> {
                _state.update { it.copy(isLoading = false, isConnectionError = true) }
            }

            is ErrorHandler.UnAuthorized -> {
                effectActionExecutor(_effect, ProductDetailsUiEffect.UnAuthorizedUserEffect)
            }

            else -> {}
        }
        updateFavoriteState(false)
    }

    override fun resetSnackBarState() {
        _state.update { it.copy(snackBar = it.snackBar.copy(isShow = false)) }
    }

    override fun onClickSeeAllReviews(productId: Long) {
        effectActionExecutor(
            _effect,
            ProductDetailsUiEffect.NavigateToReviewsScreen(productId = productId)
        )
    }

    override fun showSnackBar(massage: String) {
        _state.update { it.copy(snackBar = it.snackBar.copy(isShow = true, massage = massage)) }
    }

    override fun onclickTryAgain() {
        getData()
    }

    // endregion

    // region Update Favorite State

    private fun updateFavoriteState(isFavorite: Boolean) {
        val updatedProductUiState = _state.value.product.copy(isFavorite = isFavorite)
        _state.update { it.copy(product = updatedProductUiState) }
    }

    override
    fun onClickFavorite(productId: Long) {
        val currentProduct = _state.value.product
        val isFavorite = currentProduct.isFavorite
        val newFavoriteState = !isFavorite

        updateFavoriteState(newFavoriteState)

        if (isFavorite) {
            deleteProductFromWishList(productId)
        } else {
            addProductToWishList(productId)
        }
    }

    // endregion

    // region Delete Product From WishList

    private fun deleteProductFromWishList(productId: Long) {
        _state.update { it.copy(error = null, isConnectionError = false) }
        tryToExecute(
            { wishListOperations.operationWishListUseCase.deleteFromWishList(productId) },
            ::onDeleteWishListSuccess,
            ::onDeleteWishListError
        )
    }

    private fun onDeleteWishListSuccess(successMessage: String) {
    }

    private fun onDeleteWishListError(error: ErrorHandler) {
        _state.update { it.copy(isLoading = false) }
        if (error is ErrorHandler.NoConnection) {
            _state.update { it.copy(isLoading = false, isConnectionError = true) }
        }
    }
}