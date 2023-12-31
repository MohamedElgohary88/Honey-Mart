package org.the_chance.honeymart.domain.repository

import org.the_chance.honeymart.domain.model.AdminLogin
import org.the_chance.honeymart.domain.model.Owner
import org.the_chance.honeymart.domain.model.OwnerProfile
import org.the_chance.honeymart.domain.usecase.Tokens

interface AuthRepository {
    suspend fun loginUser(
        email: String,
        password: String,
        deviceToken: String
    ): Tokens

    suspend fun refreshToken(refreshToken: String): Tokens

    suspend fun saveTokens(accessToken: String, refreshToken: String)

    fun getAccessToken(): String?
    fun getRefreshToken(): String?

    suspend fun clearToken()

    suspend fun registerUser(fullName: String, password: String, email: String): Boolean

    suspend fun getDeviceToken(): String

    suspend fun createOwnerAccount(fullName: String, email: String, password: String): Boolean

    suspend fun loginOwner(email: String, password: String,deviceToken: String): Owner

    suspend fun saveOwnerName(name: String)
    fun getOwnerName(): String?
    suspend fun saveOwnerImageUrl(imageUrl: String)
    fun getOwnerImageUrl(): String?
    suspend fun getOwnerProfile(): OwnerProfile
    suspend fun saveOwnerMarketId(marketId: Long)
     fun getOwnerMarketId(): Long?
    suspend fun loginAdmin(email: String, password: String): AdminLogin
    suspend fun saveAdminName(name: String)
    suspend fun getAdminName(): String?
    suspend fun checkAdminAuthentication()
}