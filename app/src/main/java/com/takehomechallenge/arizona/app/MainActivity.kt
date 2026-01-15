package com.takehomechallenge.arizona.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.compose.rememberNavController
import com.takehomechallenge.arizona.presentation.navigation.NavGraph
import com.takehomechallenge.arizona.presentation.screen.MainScreen
import com.takehomechallenge.arizona.presentation.theme.ArizonaTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ArizonaTheme {
                MainScreen()
            }
        }
    }
}