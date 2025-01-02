package uk.ac.tees.mad.travelplanner.viewmodels

import android.graphics.Bitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import uk.ac.tees.mad.travelplanner.data.TripRepository

class CreateTripViewModel(private val repository: TripRepository) : ViewModel() {
    private val _createTripStatus = MutableStateFlow<CreateTripStatus>(CreateTripStatus.Idle)
    val createTripStatus: StateFlow<CreateTripStatus> = _createTripStatus.asStateFlow()

    fun createTrip(destination: String, startDate: Long, endDate: Long, itinerary: String, photos: List<Bitmap>) {
        viewModelScope.launch {
            _createTripStatus.value = CreateTripStatus.Loading
            try {
                val tripId = repository.createTrip(destination, startDate, endDate, itinerary, photos)
                _createTripStatus.value = CreateTripStatus.Success(tripId)
            } catch (e: Exception) {
                _createTripStatus.value = CreateTripStatus.Error(e.message ?: "Unknown error occurred")
            }
        }
    }
}

sealed class CreateTripStatus {
    object Idle : CreateTripStatus()
    object Loading : CreateTripStatus()
    data class Success(val tripId: String) : CreateTripStatus()
    data class Error(val message: String) : CreateTripStatus()
}