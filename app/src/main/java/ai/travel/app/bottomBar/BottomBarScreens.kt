package ai.travel.app.bottomBar

import ai.travel.app.navigation.Screens
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material.icons.outlined.PersonOutline
import androidx.compose.ui.graphics.vector.ImageVector


sealed class BottomBarScreens(val route: String?, val title: String?, val icon: ImageVector) {
    object HomeScreen : BottomBarScreens(
        Screens.Home.route,
        "Home",
        Icons.Outlined.Home
    )

    object RoutesScreen : BottomBarScreens(
        Screens.Routes.route,
        "Routes",
        Icons.Outlined.LocationOn
    )

    object ProfileScreen : BottomBarScreens(
        Screens.Profile.route,
        "Profile",
        Icons.Outlined.PersonOutline
    )
}

val items = listOf(
    BottomBarScreens.HomeScreen,
    BottomBarScreens.RoutesScreen,
    BottomBarScreens.ProfileScreen
)
