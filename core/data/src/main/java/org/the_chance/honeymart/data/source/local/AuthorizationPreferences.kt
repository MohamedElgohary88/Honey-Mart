package org.the_chance.honeymart.data.source.local

interface AuthorizationPreferences {
    suspend fun clearToken()
    suspend fun saveTokens(accessToken: String,refreshToken: String)
    fun getAccessToken(): String?
    fun getRefreshToken(): String?
    suspend fun saveOwnerName(name: String)
    fun getOwnerName(): String?
    suspend fun saveOwnerImageUrl(image: String)
    fun getOwnerImageUrl(): String?
    val storedAccessToken: String?
    val storedRefreshToken: String?
    suspend fun saveAdminName(name: String)
    fun getAdminName(): String?
}