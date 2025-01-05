package uk.ac.tees.mad.travelplanner.viewmodels

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import uk.ac.tees.mad.travelplanner.data.TripRepository
import uk.ac.tees.mad.travelplanner.data.local.TripEntity
import javax.inject.Inject

@HiltViewModel
class TripListViewModel @Inject constructor(private val repository: TripRepository) : ViewModel() {

    var tripList = mutableStateOf(emptyList<Trip>())
        private set

     fun getAllTrips() {
        viewModelScope.launch {
            repository.getAllTrips().collect {
                if (it.isEmpty()) {
                    repository.addFromFirebaseToLocal()
                }
                Log.d("TAG", it.toString())
                tripList.value = it
            }
        }
    }

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
    val photoData: List<ByteArray> = emptyList()
)


fun Trip.toTripEntity(): TripEntity {
    val currentUser = Firebase.auth.currentUser!!
    return TripEntity(
        id = id,
        startLocation = startLocation,
        destination = destination,
        startDate = startDate,
        endDate = endDate,
        itinerary = itinerary,
        isSynced = false,
        userId = currentUser.uid,
        photoData = photoData
    )
}