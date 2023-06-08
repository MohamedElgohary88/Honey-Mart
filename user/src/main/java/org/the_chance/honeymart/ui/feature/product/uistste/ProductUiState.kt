package org.the_chance.honeymart.ui.feature.product.uistste

import org.the_chance.honeymart.domain.model.Category
import org.the_chance.honeymart.domain.model.Product

data class ProductsUiState(
    val isLoading: Boolean = true,
    val isError: Boolean = false,
    val productList: List<ProductUiState> = emptyList(),
    val categoryList: List<CategoryUiState> = emptyList(),
)

data class ProductUiState(
    val id: Long = 0L,
    val price: Double = 0.0,
    val name: String = "",
    val quantity: String = "",
    val isFavorite: Boolean = false
)

data class CategoryUiState(
    val categoryId: Long = 0L,
    val imageId: Int = 0,
    val categoryName: String = ""
)

fun Category.asCategoryUiState(): CategoryUiState {
    return CategoryUiState(
        categoryId = id,
        imageId = imageId,
        categoryName = name
    )
}
fun Product.asProductUiState():ProductUiState{
    return ProductUiState(
        id = id ,
        price = price ,
        name = name ,
        quantity = quantity ,
    )

}