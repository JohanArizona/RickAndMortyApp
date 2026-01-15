package com.takehomechallenge.arizona.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.takehomechallenge.arizona.presentation.screen.detail.DetailScreen
import com.takehomechallenge.arizona.presentation.screen.home.HomeScreen
import com.takehomechallenge.arizona.presentation.screen.search.SearchScreen

@Composable
fun NavGraph(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Home.route,
        modifier = modifier
    ) {
        composable(Screen.Home.route) {
            HomeScreen(navController = navController)
        }

        composable(Screen.Search.route) {
            // TO DO
        }

        composable(
            route = Screen.Detail.route,
            arguments = listOf(navArgument("characterId") { type = NavType.IntType })
        ) { backStackEntry ->
            val characterId = backStackEntry.arguments?.getInt("characterId") ?: 0
            DetailScreen(characterId = characterId, navController = navController)
        }

        composable(Screen.Search.route) {
            SearchScreen(navController = navController)
        }
    }
}