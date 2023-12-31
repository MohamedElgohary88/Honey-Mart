package org.the_chance.honeymart.domain.usecase

import org.the_chance.honeymart.domain.repository.HoneyMartRepository
import javax.inject.Inject

class AddToCategoryUseCase @Inject constructor(
    private val honeyMartRepository: HoneyMartRepository
) {
    suspend operator fun invoke(name: String, imageID: Int): String {
        return honeyMartRepository.addCategory(name, imageID)
    }

}