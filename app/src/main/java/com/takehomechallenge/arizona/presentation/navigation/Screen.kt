package com.takehomechallenge.arizona.presentation.navigation

sealed class Screen(val route: String) {
    data object Home : Screen("home")
    data object Search : Screen("search")
    data object Favorite : Screen("favorite")
    data object Detail : Screen("detail/{characterId}") {
        fun createRoute(characterId: Int) = "detail/$characterId"
    }
}