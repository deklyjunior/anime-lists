package com.junior0028.assesstmentmobpro2

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.compose.rememberNavController
import com.junior0028.assesstmentmobpro2.navigation.NavGraph
import com.junior0028.assesstmentmobpro2.ui.theme.AssesstmentMobpro2Theme

class  MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AssesstmentMobpro2Theme {
                val navController = rememberNavController()
                NavGraph(navController = navController)
            }
        }
    }
}