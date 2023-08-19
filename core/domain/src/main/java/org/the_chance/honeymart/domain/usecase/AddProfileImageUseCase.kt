package org.the_chance.honeymart.domain.usecase

import org.the_chance.honeymart.domain.repository.HoneyMartRepository
import javax.inject.Inject

class AddProfileImageUseCase @Inject constructor(
    private val honeyMartRepository: HoneyMartRepository
) {
    suspend operator fun invoke(
        userId: Long,
        image: ByteArray
    ): String {
        return honeyMartRepository.addProfileImage(userId, image)
    }
}