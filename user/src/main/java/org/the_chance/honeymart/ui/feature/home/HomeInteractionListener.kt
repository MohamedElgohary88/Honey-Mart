package org.the_chance.honeymart.ui.feature.home

interface HomeInteractionListener {
    fun getData()

    fun onClickPagerItem(marketId: Long)

    fun onClickSeeAllMarkets()

    fun onClickGetCoupon(couponId: Long)

    fun onClickProductItem(productId: Long)

    fun onClickLastPurchases(orderId: Long)



    fun onClickSearchBar()

    fun onClickCategory(categoryId: Long, position: Int)

    fun onClickChipCategory(marketId: Long)

    fun onClickSeeAllNewProducts()

    fun onClickLastPurchasesSeeAll()

}