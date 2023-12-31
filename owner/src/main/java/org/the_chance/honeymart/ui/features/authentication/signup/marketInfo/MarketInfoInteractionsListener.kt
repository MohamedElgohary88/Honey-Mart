package org.the_chance.honeymart.ui.features.authentication.signup.marketInfo

/**
 * Created by Aziza Helmy on 8/9/2023.
 */
interface MarketInfoInteractionsListener {
    fun onClickSendButton()
    fun onClickLogout()
    fun onMarketNameInputChange(marketName: CharSequence)
    fun onMarketAddressInputChange(address: CharSequence)
    fun onMarketDescriptionInputChanged(description: CharSequence)
    fun addMarketImage(image: ByteArray)
    fun onImageSelected(uri: ByteArray)
    fun onClickRemoveSelectedImage(imageUri: ByteArray)
}