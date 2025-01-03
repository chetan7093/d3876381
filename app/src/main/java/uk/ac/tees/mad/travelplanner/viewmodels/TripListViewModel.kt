package uk.ac.tees.mad.travelplanner.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import uk.ac.tees.mad.travelplanner.data.TripRepository
import uk.ac.tees.mad.travelplanner.data.local.TripEntity
import javax.inject.Inject

@HiltViewModel
class TripListViewModel @Inject constructor(private val repository: TripRepository) : ViewModel() {
    val trips: Flow<List<Trip>> = repository.getAllTrips()

    init {
        viewModelScope.launch {
            repository.syncUnSyncedTrips()
        }
    }
}

data class Trip(
    val id: String = "",
    val startLocation: String = "",
    val destination: String = "",
    val startDate: Long = 0L,
    val endDate: Long = 0L,
    val itinerary: String = "",
    val photoUrl: List<String> = emptyList()
)

fun Trip.toTripEntity() = TripEntity(
    id = id,
    startLocation = startLocation,
    destination = destination,
    startDate = startDate,
    endDate = endDate,
    itinerary = itinerary,
    photoUrls = photoUrl,
    isSynced = false
)