package uk.ac.tees.mad.travelplanner.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import uk.ac.tees.mad.travelplanner.data.TripRepository
import javax.inject.Inject

@HiltViewModel
class TripDetailsViewModel @Inject constructor(
    private val repository: TripRepository
) : ViewModel() {
    private val _trip = MutableStateFlow<Trip?>(null)
    val trip: StateFlow<Trip?> = _trip.asStateFlow()

    fun loadTrip(id: String) {
        viewModelScope.launch {
            repository.getTripById(id).collect {
                _trip.value = it
            }
        }
    }
}