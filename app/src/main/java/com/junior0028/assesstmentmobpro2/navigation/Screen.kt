package com.junior0028.assesstmentmobpro2.navigation

sealed class Screen(val route: String) {
    object Main : Screen("main")
    object Detail : Screen("detail/{animeId}") {
        fun createRoute(animeId: Long) = "detail/$animeId"
    }
    object RecycleBin : Screen("recycle_bin")
}
