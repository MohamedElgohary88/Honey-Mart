package org.the_chance.honeymart.ui.features.category

import org.the_chance.honeymart.domain.model.CategoryEntity
import org.the_chance.honeymart.domain.util.ErrorHandler
import org.the_chance.honeymart.ui.addCategory.AddCategoryUIState
import org.the_chance.honeymart.ui.addCategory.CategoryImageUIState

/**
 * Created by Aziza Helmy on 8/7/2023.
 */
data class CategoriesUiState(
    val isLoading: Boolean = true,
    val isError: Boolean = false,
    val error: ErrorHandler? = null,
    val message: String = "",
    val nameCategory: String = "",
    val position: Int = 0,
    val categoryImageId: Int = 0,
    val showAddCategory: Boolean = false,
    val categoryImages: List<CategoryImageUIState> = emptyList(),
    val addCategoryUiState: AddCategoryUIState = AddCategoryUIState(),
    val categories: List<CategoryUiState> = emptyList(),
    val categoryId: Long = 0L
)
data class CategoryUiState(
    val categoryId: Long = 0L,
    val categoryName: String = "",
    val categoryImageId: Int = 0,
    val isCategorySelected: Boolean = false
)

fun CategoryEntity.toCategoryUiState(): CategoryUiState {
    return CategoryUiState(
        categoryId = categoryId,
        categoryName = categoryName,
        categoryImageId = categoryImageId
    )
}

fun CategoriesUiState.showLazyCondition() = !this.isLoading && !this.isError