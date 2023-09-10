package org.the_chance.honeymart.ui.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.navigation
import org.the_chance.honeymart.ui.feature.coupons.couponsRoute
import org.the_chance.honeymart.ui.feature.notifications.notificationsRoute
import org.the_chance.honeymart.ui.feature.orders.orderRoute
import org.the_chance.honeymart.ui.feature.profile.profileRoute

fun NavGraphBuilder.profileNavGraph() {
    navigation(
        startDestination = Screen.ProfileScreen.route,
        route = Graph.PROFILE
    ) {
        profileRoute()
        orderRoute()
        notificationsRoute()
        couponsRoute()
    }
}