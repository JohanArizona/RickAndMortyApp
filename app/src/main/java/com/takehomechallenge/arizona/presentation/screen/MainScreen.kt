package com.takehomechallenge.arizona.presentation.screen

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.takehomechallenge.arizona.presentation.navigation.BottomNavigationBar
import com.takehomechallenge.arizona.presentation.navigation.NavGraph
import com.takehomechallenge.arizona.presentation.navigation.Screen
import kotlinx.coroutines.launch

@Composable
fun MainScreen() {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    val scope = rememberCoroutineScope()

    val homeListState = rememberLazyGridState()
    val searchListState = rememberLazyGridState()
    val favoriteListState = rememberLazyGridState()

    val showBottomBar = currentRoute in listOf(
        Screen.Home.route,
        Screen.Search.route,
        Screen.Favorite.route
    )

    Scaffold(
        bottomBar = {
            if (showBottomBar) {
                BottomNavigationBar(
                    navController = navController,
                    onReselect = { screen ->
                        scope.launch {
                            when (screen) {
                                Screen.Home -> homeListState.animateScrollToItem(0)
                                Screen.Search -> searchListState.animateScrollToItem(0)
                                Screen.Favorite -> favoriteListState.animateScrollToItem(0)
                                else -> {}
                            }
                        }
                    }
                )
            }
        }
    ) { paddingValues ->
        NavGraph(
            navController = navController,
            modifier = Modifier.padding(paddingValues),
            homeListState = homeListState,
            searchListState = searchListState,
            favoriteListState = favoriteListState
        )
    }
}