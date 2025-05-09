package com.junior0028.assesstmentmobpro2.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.junior0028.assesstmentmobpro2.ui.theme.screen.DetailScreen
import com.junior0028.assesstmentmobpro2.ui.theme.screen.MainScreen
import com.junior0028.assesstmentmobpro2.ui.theme.screen.RecycleBinScreen

@Composable
fun NavGraph(navController: NavHostController = rememberNavController()) {
    NavHost(navController = navController, startDestination = Screen.Main.route) {
        composable(Screen.Main.route) {
            MainScreen(navController)
        }
        composable(Screen.RecycleBin.route) {
            RecycleBinScreen(navController)
        }
        composable("detail/{animeId}") { backStackEntry ->
            val animeId = backStackEntry.arguments?.getString("animeId")?.toLongOrNull()
            animeId?.let {
                DetailScreen(
                    animeId = it,
                    navController = navController
                )
            }
        }
    }
}
