package org.the_chance.honeymart.data.source.remote.mapper

import org.the_chance.honeymart.data.source.remote.models.RecentProductDto
import org.the_chance.honeymart.domain.model.RecentProduct

fun RecentProductDto.toRecentProduct() = RecentProduct(
    productId = id ?: 0L,
    productName =  name ?: "",
    productDescription = description ?: "",
    productPrice = price ?: 0.0,
    productImages = images ?: emptyList()
)