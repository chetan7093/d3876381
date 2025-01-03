package uk.ac.tees.mad.travelplanner.viewmodels

import android.graphics.Bitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import uk.ac.tees.mad.travelplanner.data.AuthRepository
import javax.inject.Inject

@HiltViewModel
class EditProfileViewModel @Inject constructor(
    private val userRepository: AuthRepository
) : ViewModel() {

    private val _user = MutableStateFlow<TPUser?>(null)
    val user: StateFlow<TPUser?> = _user.asStateFlow()

    init {
        loadUserProfile()
    }

    private fun loadUserProfile() {
        viewModelScope.launch {
            userRepository.getCurrentUser().onSuccess {
                _user.value = it
            }.onFailure {
                it.printStackTrace()
            }
        }
    }

    suspend fun updateProfile(name: String, profilePicture: Bitmap?): Result<Unit> {
        return userRepository.updateCurrentUser(name, profilePicture)
    }
}
