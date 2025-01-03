package uk.ac.tees.mad.travelplanner.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.auth.User
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import uk.ac.tees.mad.travelplanner.data.AuthRepository
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val userRepository: AuthRepository
) : ViewModel() {

    private val _user = MutableStateFlow<TPUser?>(null)
    val user: StateFlow<TPUser?> = _user.asStateFlow()

    init {
        loadUserProfile()
    }

    private fun loadUserProfile() {
        viewModelScope.launch {
            userRepository.getCurrentUser().onSuccess { user ->
                _user.value = user
            }.onFailure {
                it.printStackTrace()
            }
        }
    }

    fun logout() {
        userRepository.signOut()
    }
}

data class TPUser(
    val name: String? = null,
    val profileUrl: String? = null,
    val email: String? = null
)
