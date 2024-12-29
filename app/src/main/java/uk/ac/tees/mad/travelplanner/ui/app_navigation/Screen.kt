package uk.ac.tees.mad.travelplanner.ui.app_navigation

sealed class Screen(val route: String) {
    object Splash : Screen("splash")
    object Login : Screen("login")
    object Register : Screen("register")
    object TripList : Screen("tripList")
    object TripDetails : Screen("tripDetails/{tripId}") {
        fun createRoute(tripId: String) = "tripDetails/$tripId"
    }
    object CreateTrip : Screen("createTrip")
    object Profile : Screen("profile")
    object EditProfile : Screen("editProfile")
}