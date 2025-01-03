package uk.ac.tees.mad.travelplanner.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import uk.ac.tees.mad.travelplanner.ui.app_navigation.Screen
import uk.ac.tees.mad.travelplanner.viewmodels.Trip
import uk.ac.tees.mad.travelplanner.viewmodels.TripListViewModel
import java.text.SimpleDateFormat
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TripListScreen(
    navController: NavHostController,
    viewModel: TripListViewModel = hiltViewModel()
) {
    val trips by viewModel.trips.collectAsState(initial = emptyList())

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("My Trips") },
                actions = {
                    IconButton(onClick = {
                        navController.navigate(Screen.CreateTrip.route)
                    }) {
                        Icon(Icons.Default.Add, contentDescription = "Add Trip")
                    }
                    IconButton(onClick = {
                        navController.navigate(Screen.Profile.route)
                    }) {
                        Icon(Icons.Default.Person, contentDescription = "Profile")
                    }
                }
            )
        }
    ) { pad ->
        LazyColumn(Modifier.padding(pad)) {
            items(trips) { trip ->
                TripListItem(
                    trip = trip,
                    onClick = { navController.navigate(Screen.TripDetails.createRoute(trip.id)) }
                )
            }
        }
    }
}

@Composable
fun TripListItem(trip: Trip, onClick: () -> Unit) {
    ElevatedCard(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        colors = CardDefaults.elevatedCardColors(Color(0xFFF7F7F7)),
        elevation = CardDefaults.elevatedCardElevation(6.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(modifier = Modifier.height(IntrinsicSize.Max)) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Box(
                        modifier = Modifier
                            .size(20.dp)
                            .clip(CircleShape)
                            .background(Color.Red)
                    )
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .width(2.dp)
                            .background(MaterialTheme.colorScheme.primary)
                    )
                    Box(
                        modifier = Modifier
                            .size(20.dp)
                            .clip(CircleShape)
                            .background(Color.Green)
                    )
                }
                Spacer(modifier = Modifier.width(16.dp))
                Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                    Text(text = trip.startLocation, style = MaterialTheme.typography.titleLarge)
                    Text(text = trip.destination, style = MaterialTheme.typography.titleLarge)
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            val dateFormat = SimpleDateFormat("dd/MMM/yyyy", Locale.getDefault())
            Text(
                text = "${dateFormat.format(trip.startDate)} - ${dateFormat.format(trip.endDate)}",
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}
