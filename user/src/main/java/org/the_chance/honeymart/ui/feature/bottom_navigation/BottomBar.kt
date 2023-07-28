package org.the_chance.honeymart.ui.feature.bottom_navigation

import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.compose.currentBackStackEntryAsState
import org.the_chance.honeymart.ui.LocalNavigationProvider
import org.the_chance.honeymart.ui.navigation.Screen

@Composable
fun BottomBar() {
    val navController = LocalNavigationProvider.current

    val screens = listOf(
        BottomBarScreen.Market,
        BottomBarScreen.Cart,
        BottomBarScreen.Order,
        BottomBarScreen.WishList,
    )
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    val selectedScreen = currentDestination?.route ?: Screen.MarketScreen.route

    if (selectedScreen == Screen.MarketScreen.route ||
        selectedScreen == Screen.CartScreen.route ||
        selectedScreen == Screen.OrderScreen.route ||
        selectedScreen == Screen.WishListScreen.route
    ) {
        NavigationBar(
            containerColor = Color.White,
        ) {
            screens.forEach { screen ->
                val selected =
                    currentDestination?.hierarchy?.any { it.route == screen.route } == true
                NavigationBarItem(
                    icon = {
                        Icon(
                            painter =
                            if (selected) painterResource(id = screen.selectedIcon)
                            else painterResource(id = screen.unSelectedIcon),
                            contentDescription = "Navigation icon"
                        )
                    },
                    selected = selected,
                    onClick = {
                        navController.navigate(screen.route)
                    },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = MaterialTheme.colorScheme.primary,
                        indicatorColor = Color.White,
                        unselectedIconColor = MaterialTheme.colorScheme.primary
                    )
                )
            }
        }
    }

}