package dev.android.innerinsight.router


private data object Route {
    const val LANDING_SCREEN = "landing_screen"
    const val HOME_SCREEN = "home_screen"
    const val CONTENT_DETAIL_SCREEN = "content_detail_screen"
}

sealed class Screen(val route: String) {
    data object LandingScreen : Screen(route = Route.LANDING_SCREEN)
    data object HomeScreen : Screen(route = Route.HOME_SCREEN)
    data object ContentDetailScreen : Screen(route = Route.CONTENT_DETAIL_SCREEN)
}