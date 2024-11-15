package uk.ac.tees.mad.travelplanner

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import dagger.hilt.android.AndroidEntryPoint
import uk.ac.tees.mad.travelplanner.ui.app_navigation.AppNavigation
import uk.ac.tees.mad.travelplanner.ui.theme.TravelPLannerTheme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TravelPLannerTheme {
                AppNavigation()
            }
        }
    }
}