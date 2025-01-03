package uk.ac.tees.mad.travelplanner.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import uk.ac.tees.mad.travelplanner.data.AuthRepository
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<UiState>(UiState.Initial)
    val uiState = _uiState.asStateFlow()

    fun signUp(name: String, email: String, password: String) {
        viewModelScope.launch {
            _uiState.value = UiState.Loading
            val result = authRepository.signUp(name, email, password)
            _uiState.value = when {
                result.isSuccess -> UiState.Success
                else -> UiState.Error(
                    result.exceptionOrNull()?.message ?: "Unknown error occurred"
                )
            }
        }
    }

    fun signIn(email: String, password: String) {
        viewModelScope.launch {
            _uiState.value = UiState.Loading
            val result = authRepository.signIn(email, password)
            _uiState.value = when {
                result.isSuccess -> UiState.Success
                else -> UiState.Error(
                    result.exceptionOrNull()?.message ?: "Unknown error occurred"
                )
            }
        }
    }
}

sealed class UiState {
    object Initial : UiState()
    object Loading : UiState()
    object Success : UiState()
    data class Error(val message: String) : UiState()
}