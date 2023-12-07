package ai.travel.app.navigation

import ai.travel.app.home.ui.HomeScreen
import ai.travel.app.home.HomeViewModel
import ai.travel.app.home.ui.PfScreen
import ai.travel.app.home.ui.RtScreen
import ai.travel.app.tripDetails.TripDetailsScreen
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable

@Composable
fun NavController(navHostController: NavHostController, paddingValues: PaddingValues) {

    val homeViewModel: HomeViewModel = hiltViewModel()

    NavHost(navController = navHostController, startDestination = Screens.Home.route) {
        composable(Screens.Home.route) {
            TripDetailsScreen(viewModel = homeViewModel, paddingValues = paddingValues)
        }
        composable(Screens.Profile.route) {
            PfScreen()
        }
        composable(Screens.Routes.route) {
            RtScreen()
        }
    }

}