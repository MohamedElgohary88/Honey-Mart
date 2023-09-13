package org.the_chance.honeymart.ui.navigation

sealed class Screen(val route: String) {
    object Orders : Screen("Orders")
    object Category : Screen("Category")
    object Login : Screen("Login")
    object Signup : Screen("Signup")
    object MarketInfo : Screen("MarketInfo")
    object Coupons : Screen("Coupons")
    object Profile : Screen("Profile")
    object WaitingApprove : Screen("WaitingApprove")
}
