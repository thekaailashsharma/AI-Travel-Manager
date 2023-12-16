package ai.travel.app.navigation

sealed class Screens(val route: String) {
    object Home : Screens("home")
    object Routes : Screens("routes")
    object Profile : Screens("profile")
    object TripDetails : Screens("tripDetails")
    object Login : Screens("login")
    object ReorderTrip : Screens("reorderTrip")
    object NewTrip : Screens("newTrip")
}