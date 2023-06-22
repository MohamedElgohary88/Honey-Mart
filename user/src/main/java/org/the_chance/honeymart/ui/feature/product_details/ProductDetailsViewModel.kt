package org.the_chance.honeymart.ui.feature.product_details

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.the_chance.honeymart.domain.usecase.AddToCartUseCase
import org.the_chance.honeymart.domain.usecase.AddToWishListUseCase
import org.the_chance.honeymart.domain.usecase.DeleteFromWishListUseCase
import org.the_chance.honeymart.domain.usecase.GetIfProductInWishListUseCase
import org.the_chance.honeymart.domain.usecase.GetProductDetailsUseCase
import org.the_chance.honeymart.domain.util.UnAuthorizedException
import org.the_chance.honeymart.ui.base.BaseViewModel
import org.the_chance.honeymart.ui.feature.uistate.ProductDetailsUiState
import org.the_chance.honeymart.ui.feature.uistate.ProductUiState
import org.the_chance.honeymart.ui.feature.uistate.toProductUiState
import org.the_chance.honeymart.util.EventHandler
import javax.inject.Inject

@HiltViewModel
class ProductDetailsViewModel @Inject constructor(
    private val getProductDetailsUseCase: GetProductDetailsUseCase,
    private val addProductToCartUseCase: AddToCartUseCase,
    private val addProductToWishListUseCase: AddToWishListUseCase,
    private val getIfProductInWishListUseCase: GetIfProductInWishListUseCase,
    private val deleteProductFromWishListUseCase: DeleteFromWishListUseCase,
    savedStateHandle: SavedStateHandle
) : BaseViewModel<ProductDetailsUiState, ProductDetailsUiEffect>(ProductDetailsUiState()),
    ProductImageInteractionListener {

    override val TAG: String = this::class.simpleName.toString()
    private val args = ProductDetailsFragmentArgs.fromSavedStateHandle(savedStateHandle)


    init {
        getProductDetails(args.productId)
    }

    // region Product
    private fun getProductDetails(productId: Long) {
        _state.update { it.copy(isLoading = true) }
        tryToExecute(
            { getProductDetailsUseCase(productId).toProductUiState() },
            ::onGetProductSuccess,
            ::onGetProductError,
        )
    }

    private fun onGetProductSuccess(product: ProductUiState) {
        checkIfProductInWishList(args.productId)
        _state.update {
            it.copy(
                isLoading = false,
                isError = false,
                product = product,
                image = product.productImages?.first() ?: "",
                smallImages = product.productImages?.drop(1) as List<String>
            )
        }
    }

    private fun onGetProductError(throwable: Throwable) {
        _state.update { it.copy(isLoading = false, isError = true) }
    }

    override fun onClickImage(url: String) {
        val newList = mutableListOf<String>()
        newList.addAll(_state.value.smallImages.filter { it != url })
        newList.add(0, _state.value.image)
        _state.update { it.copy(smallImages = newList) }
        _state.update { it.copy(image = url) }
    }

    fun addProduct() {
        val currentQuantity = _state.value.quantity
        val newQuantity = currentQuantity + 1
        _state.update { it.copy(quantity = newQuantity) }
    }

    fun removeProduct() {
        val currentQuantity = _state.value.quantity
        val newQuantity = if (currentQuantity > 0) currentQuantity - 1 else 0
        _state.update { it.copy(quantity = newQuantity) }
    }

    // endregion

    // region Cart

    fun addProductToCart(productId: Long, count: Int) {
        _state.update { it.copy(isAddToCartLoading = true) }
        tryToExecute(
            { addProductToCartUseCase(productId, count) },
            ::onAddProductToCartSuccess,
            ::onAddProductToCartError
        )
    }

    private fun onAddProductToCartSuccess(message: String) {
        _state.update { it.copy(isAddToCartLoading = false) }
        viewModelScope.launch {
            _effect.emit(EventHandler(ProductDetailsUiEffect.AddToCartSuccess(message)))
        }
    }

    private fun onAddProductToCartError(error: Exception) {
        _state.update { it.copy(isAddToCartLoading = false, isError = true) }
        if (error is UnAuthorizedException) {
            viewModelScope.launch {
                _effect.emit(EventHandler(ProductDetailsUiEffect.UnAuthorizedUserEffect))
            }
        } else {
            viewModelScope.launch {
                _effect.emit(EventHandler(ProductDetailsUiEffect.AddToCartError(error)))
            }
        }
    }

    // endregion

    // region Wishlist


    // region Check If Product In Wishlist

    private fun checkIfProductInWishList(productId: Long) {
        tryToExecute(
            { getIfProductInWishListUseCase(productId) },
            ::onGetIfProductInWishListSuccess,
            ::onGetIfProductInWishListError
        )
    }

    private fun onGetIfProductInWishListSuccess(isFavorite: Boolean) {
        updateFavoriteState(isFavorite)
    }

    private fun onGetIfProductInWishListError(error: Exception) {
        log("something went wrong with getWithListProduct $error")
    }

    // endregion

    // region Add Product To Wishlist

    private fun addProductToWishList(productId: Long) {
        tryToExecute(
            { addProductToWishListUseCase(productId) },
            ::onAddProductToWishListSuccess,
            { error -> onAddProductToWishListError(error, productId) }
        )
    }

    private fun onAddProductToWishListSuccess(message: String) {
        viewModelScope.launch {
            _effect.emit(
                EventHandler(ProductDetailsUiEffect.AddProductToWishListEffectSuccess(message))
            )
        }
    }

    private fun onAddProductToWishListError(error: Exception, productId: Long) {
        if (error is UnAuthorizedException) {
            viewModelScope.launch {
                _effect.emit(EventHandler(ProductDetailsUiEffect.UnAuthorizedUserEffect))
            }
        } else {
            viewModelScope.launch {
                _effect.emit(
                    EventHandler(ProductDetailsUiEffect.AddProductToWishListEffectError(error))
                )
            }
        }
        updateFavoriteState(false)
    }

    // endregion

    // region Update Favorite State

    private fun updateFavoriteState(isFavorite: Boolean) {
        val updatedProductUiState = _state.value.product.copy(isFavorite = isFavorite)
        _state.update { it.copy(product = updatedProductUiState) }
    }

    fun onClickFavIcon(productId: Long) {
        val currentProduct = _state.value.product
        val isFavorite = currentProduct.isFavorite ?: false
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
        tryToExecute(
            { deleteProductFromWishListUseCase(productId) },
            ::onDeleteWishListSuccess,
            ::onDeleteWishListError
        )
    }


    private fun onDeleteWishListSuccess(successMessage: String) {
        viewModelScope.launch {
            _effect.emit(
                EventHandler(
                    ProductDetailsUiEffect.RemoveProductFromWishListEffectSuccess(successMessage)
                )
            )
        }
        log("Deleted Successfully : $successMessage")
    }

    private fun onDeleteWishListError(error: Exception) {
        viewModelScope.launch {
            _effect.emit(
                EventHandler(ProductDetailsUiEffect.RemoveProductFromWishListEffectError(error))
            )
        }
        log("Delete From WishList Error : ${error.message}")
    }

    // endregion

    // endregion
}