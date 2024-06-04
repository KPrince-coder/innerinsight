package dev.android.innerinsight.router

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.EaseIn
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import dev.android.innerinsight.screens.ContentDetailScreen
import dev.android.innerinsight.screens.HomeScreen
import dev.android.innerinsight.screens.LandingScreen

@Composable
fun ScreenManager() {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = Screen.LandingScreen.route
    ) {
        composable(
            Screen.LandingScreen.route,
            enterTransition = {
                slideIntoContainer(
                    animationSpec = tween(
                        300, easing = LinearEasing
                    ),
                    towards = AnimatedContentTransitionScope.SlideDirection.Down
                ).plus(
                    fadeIn(
                        animationSpec = tween(
                            easing = EaseIn, durationMillis = 400
                        )
                    )
                )
            }
        ) {
            LandingScreen(navController)
        }
        composable(
            Screen.HomeScreen.route,
            enterTransition = {
                fadeIn(
                    animationSpec = tween(
                        500, easing = LinearEasing
                    )
                )
            },
            popExitTransition = {
                fadeOut(
                    animationSpec = tween(
                        500, easing = LinearEasing
                    )
                )
            }
        ) {
            HomeScreen(navController)
        }

        composable(
            Screen.ContentDetailScreen.route + "/{key}",
            arguments = listOf(navArgument("key") {
                type = NavType.IntType
            })
        ) { navBackStack ->
            val day = navBackStack.arguments?.getInt("key")!!
            ContentDetailScreen(day = day) {
                navController.popBackStack()
            }
        }
    }
}