package org.the_chance.honeymart.domain.usecase

import org.the_chance.honeymart.domain.model.WishListEntity
import org.the_chance.honeymart.domain.repository.HoneyMartRepository
import javax.inject.Inject

class GetAllWishListUseCase  @Inject constructor(
    private val honeyMartRepository: HoneyMartRepository
) {
    suspend operator fun invoke():List<WishListEntity> =
        honeyMartRepository.getWishList() ?: emptyList()
}