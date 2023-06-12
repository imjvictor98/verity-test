package br.com.cvj.veritytest.ui.user.input

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import br.com.cvj.veritytest.model.data.UserInfoResponse
import br.com.cvj.veritytest.model.repository.user.info.UserInfoRepository
import br.com.cvj.veritytest.util.CloseableCoroutineScope
import br.com.cvj.veritytest.util.CustomIdlingResource
import br.com.cvj.veritytest.util.DefaultDispatcherProvider
import br.com.cvj.veritytest.util.DispatcherProvider
import br.com.cvj.veritytest.util.exception.IllegalViewModelException
import br.com.cvj.veritytest.util.extension.onError
import br.com.cvj.veritytest.util.extension.onException
import br.com.cvj.veritytest.util.extension.onSuccess
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class UserInputViewModel(
    private val userInputDataSource: UserInfoRepository,
    private val dispatcher: CoroutineDispatcher = DefaultDispatcherProvider().main
): ViewModel() {

    class UserViewModelFactory(private val userDataSource: UserInfoRepository):
    ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(UserInputViewModel::class.java)) {
                return UserInputViewModel(userDataSource) as T
            }
            throw IllegalViewModelException()
        }
    }

    private val _uiState: MutableStateFlow<UserInputUiState> =
        MutableStateFlow(UserInputUiState.Initial)

    val uiState: StateFlow<UserInputUiState> = _uiState

    fun getUserInfo(username: String) {
        CustomIdlingResource.increment(this)

        CoroutineScope(dispatcher).launch {
            val response = userInputDataSource.getUser(username)

            response.onSuccess {
                _uiState.value = UserInputUiState.Success(it)
                CustomIdlingResource.decrement(this)
            }.onError { code, _ ->
                _uiState.value = UserInputUiState.Error(true, code = code)
                CustomIdlingResource.decrement(this)
            }.onException {
                _uiState.value = UserInputUiState.Error(true)
                CustomIdlingResource.decrement(this)
            }
        }
    }

    fun onUserSaved(userInfoResponse: UserInfoResponse) {
        _uiState.value = UserInputUiState.Success(userInfoResponse)
    }
}

sealed class UserInputUiState {
    data class Success(val userInfo: UserInfoResponse): UserInputUiState()
    data class Error(val showError: Boolean, val code: Int? = -1): UserInputUiState()
    object Initial: UserInputUiState()
}