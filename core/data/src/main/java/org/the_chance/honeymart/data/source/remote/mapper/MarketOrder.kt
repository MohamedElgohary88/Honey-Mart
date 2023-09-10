package org.the_chance.honeymart.data.source.remote.mapper

import org.the_chance.honeymart.data.source.remote.models.MarketOrderDto
import org.the_chance.honeymart.data.source.remote.models.UserDto
import org.the_chance.honeymart.data.source.remote.util.convertTimestampToDate
import org.the_chance.honeymart.domain.model.Market
import org.the_chance.honeymart.domain.model.UserFields
import java.util.Date

fun MarketOrderDto.toMarketOrder(): Market.Order {
    return Market.Order(
        orderId = orderId ?: 0L,
        totalPrice = totalPrice ?: 0.0,
        state = state ?: 0,
        date = date?.convertTimestampToDate() ?: Date(),
        user = user.toUserFields(),
    )
}

fun UserDto.toUserFields() = UserFields(
    userId = userId,
    fullName = fullName
)
