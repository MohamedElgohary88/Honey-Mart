package org.the_chance.honeymart.ui.features.category

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import org.the_chance.honeymart.ui.navigation.Graph
import org.the_chance.honeymart.ui.navigation.Screen

private val ROUTE = Screen.Category.route

fun NavController.navigateToCategoryScreen() {
    navigate(ROUTE){
        popUpTo(route = Graph.AUTH_GRAPH){
            inclusive = true
        }
        launchSingleTop = true
    }
}

fun NavGraphBuilder.categoryRoute() {
    composable(
        route = ROUTE,
    ) {
        CategoriesScreen()
    }
}