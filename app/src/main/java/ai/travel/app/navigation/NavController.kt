package ai.travel.app.navigation

import ai.travel.app.home.ui.HomeScreen
import ai.travel.app.home.HomeViewModel
import ai.travel.app.home.ui.HomeScreenMain
import ai.travel.app.home.ui.NewHomeScreen
import ai.travel.app.home.ui.PfScreen
import ai.travel.app.home.ui.RtScreen
import ai.travel.app.mapsSearch.MapsSearchViewModel
import ai.travel.app.mapsSearch.ui.MapsSearchScreen
import ai.travel.app.newTrip.NewTripScreen
import ai.travel.app.newTrip.NewTripViewModel
import ai.travel.app.profile.NewProfileScreen
import ai.travel.app.riveanimation.LoginUI
import ai.travel.app.tripDetails.ReorderLists
import ai.travel.app.tripDetails.TripDetailsScreen
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.BottomSheetScaffoldState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NavController(
    navHostController: NavHostController,
    paddingValues: PaddingValues,
    isBottomBarVisible: MutableState<Boolean>
) {

    val homeViewModel: HomeViewModel = hiltViewModel()
    val newTripViewModel: NewTripViewModel = hiltViewModel()
    val mapsViewModel: MapsSearchViewModel = hiltViewModel()
    val loginStatus = homeViewModel.loginStatus.collectAsState()

    NavHost(navController = navHostController, startDestination = if (loginStatus.value) Screens.Home.route else Screens.Login.route) {
        composable(Screens.Home.route) {
            HomeScreenMain(
                viewModel = homeViewModel,
                bottomBarPadding = paddingValues,
                newTripViewModel = newTripViewModel,
                isBottomBarVisible = isBottomBarVisible,
                navController = navHostController
            )
//            TripDetailsScreen(viewModel = homeViewModel, paddingValues = paddingValues)
        }
        composable(Screens.Profile.route) {
            NewProfileScreen(navHostController)
        }
        composable(Screens.Routes.route) {
            MapsSearchScreen(viewModel = mapsViewModel, navController = navHostController)
        }

        composable(Screens.TripDetails.route) {
            TripDetailsScreen(viewModel = homeViewModel, paddingValues = paddingValues, navController = navHostController)
        }

        composable(Screens.Login.route) {
            LoginUI(paddingValues = paddingValues, viewModel = homeViewModel, navController = navHostController)
        }

        composable(Screens.ReorderTrip.route) {
            ReorderLists(viewModel = homeViewModel, paddingValues = paddingValues)
        }

        composable(Screens.NewTrip.route) {
            NewTripScreen(viewModel = newTripViewModel, homeViewModel = homeViewModel, navController = navHostController)
        }
    }

}