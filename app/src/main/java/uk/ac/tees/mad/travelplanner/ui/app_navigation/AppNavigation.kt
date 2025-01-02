package uk.ac.tees.mad.travelplanner.ui.app_navigation

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import uk.ac.tees.mad.travelplanner.ui.screens.CreateTripScreen
import uk.ac.tees.mad.travelplanner.ui.screens.LoginScreen
import uk.ac.tees.mad.travelplanner.ui.screens.SignUpScreen
import uk.ac.tees.mad.travelplanner.ui.screens.SplashScreen
import uk.ac.tees.mad.travelplanner.ui.screens.TripDetailsScreen
import uk.ac.tees.mad.travelplanner.ui.screens.TripListScreen

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = Screen.Splash.route) {
        composable(Screen.Splash.route) {
            SplashScreen(navController)
        }
        composable(Screen.Login.route) {
            LoginScreen(navController)
        }
        composable(Screen.Register.route) {
            SignUpScreen(navController)
        }
        composable(Screen.TripList.route) {
            TripListScreen(navController)
        }
        composable(Screen.CreateTrip.route) {
            CreateTripScreen(navController)
        }
        composable(Screen.TripDetails.route) { entry ->
            val tripId = entry.arguments?.getString("tripId") ?: return@composable
            TripDetailsScreen(tripId = tripId, navController)
        }
    }
}
