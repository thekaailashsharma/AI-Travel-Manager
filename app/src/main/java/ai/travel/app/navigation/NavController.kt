package ai.travel.app.navigation

import ai.travel.app.home.ui.HomeScreen
import ai.travel.app.home.ui.PfScreen
import ai.travel.app.home.ui.RtScreen
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable

@Composable
fun NavController(navHostController: NavHostController) {

    NavHost(navController = navHostController, startDestination = Screens.Home.route) {
        composable(Screens.Home.route) {
            HomeScreen()
        }
        composable(Screens.Profile.route) {
            PfScreen()
        }
        composable(Screens.Routes.route) {
            RtScreen()
        }
    }

}