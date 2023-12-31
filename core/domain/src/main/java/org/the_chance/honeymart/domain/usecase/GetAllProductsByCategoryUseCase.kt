package org.the_chance.honeymart.domain.usecase

import org.the_chance.honeymart.domain.model.Product
import org.the_chance.honeymart.domain.repository.HoneyMartRepository
import javax.inject.Inject

class GetAllProductsByCategoryUseCase @Inject constructor(
    private val honeyMartRepository: HoneyMartRepository
) {
    suspend operator fun invoke(categoryId: Long, page: Int): List<Product> =
        honeyMartRepository.getAllProductsByCategory(page = page, categoryId)
}