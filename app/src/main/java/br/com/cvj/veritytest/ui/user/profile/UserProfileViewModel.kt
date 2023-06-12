package br.com.cvj.veritytest.ui.user.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import br.com.cvj.veritytest.model.data.UserRepositoryItemResponse
import br.com.cvj.veritytest.model.network.NetworkResult
import br.com.cvj.veritytest.model.repository.user.profile.UserProfileRepository
import br.com.cvj.veritytest.util.CloseableCoroutineScope
import br.com.cvj.veritytest.util.CustomIdlingResource
import br.com.cvj.veritytest.util.DefaultDispatcherProvider
import br.com.cvj.veritytest.util.exception.IllegalViewModelException
import br.com.cvj.veritytest.util.extension.whenIsEmpty
import br.com.cvj.veritytest.util.extension.whenIsNotEmpty
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class UserProfileViewModel(
    private val userProfileDataSource: UserProfileRepository,
    private val dispatcher: CoroutineDispatcher = DefaultDispatcherProvider().main
) : ViewModel() {

    class ViewModelFactory(private val userProfileDataSource: UserProfileRepository) :
        ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(UserProfileViewModel::class.java)) {
                return UserProfileViewModel(userProfileDataSource) as T
            }
            throw IllegalViewModelException()
        }
    }

    private val _uiState: MutableStateFlow<UserProfileUiState> =
        MutableStateFlow(UserProfileUiState.Loading(true))

    val uiState: StateFlow<UserProfileUiState> = _uiState

    fun getUserRepositories(username: String) {
        _uiState.value = UserProfileUiState.Loading(true)
        _uiState.value = UserProfileUiState.Reload(false)

        CoroutineScope(dispatcher).launch {
            val response = userProfileDataSource.getRepositories(username)

            with(response) networkResult@ {
                if (this@networkResult !is NetworkResult.Success) {
                    _uiState.value = UserProfileUiState.Loading(false)

                    _uiState.value = UserProfileUiState.Error()

                    _uiState.value = UserProfileUiState.Reload(false)

                    CustomIdlingResource.decrement(this)
                } else {
                    _uiState.value = UserProfileUiState.Loading(false)

                    data.whenIsNotEmpty { _uiState.value = UserProfileUiState.Success(data) }
                        .whenIsEmpty { _uiState.value = UserProfileUiState.Empty(true) }

                    _uiState.value = UserProfileUiState.Reload(true)

                    CustomIdlingResource.decrement(this)
                }
            }
        }
    }
}

sealed class UserProfileUiState {
    data class Success(val repositories: List<UserRepositoryItemResponse>) : UserProfileUiState()
    data class Error(private val error: Throwable? = null) : UserProfileUiState()
    data class Loading(val isLoading: Boolean) : UserProfileUiState()
    data class Empty(val isEmpty: Boolean) : UserProfileUiState()
    data class Reload(val showReload: Boolean) : UserProfileUiState()
}
