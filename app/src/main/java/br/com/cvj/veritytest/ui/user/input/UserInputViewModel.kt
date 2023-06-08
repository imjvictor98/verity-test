package br.com.cvj.veritytest.ui.user.input

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import br.com.cvj.veritytest.model.data.UserInfoResponse
import br.com.cvj.veritytest.model.network.NetworkResult
import br.com.cvj.veritytest.model.repository.user.info.UserInfoRepository
import br.com.cvj.veritytest.util.CloseableCoroutineScope
import br.com.cvj.veritytest.util.CustomIdlingResource
import kotlinx.coroutines.launch

class UserInputViewModel(
    private val userDataSource: UserInfoRepository,
    private val coroutineScope: CloseableCoroutineScope = CloseableCoroutineScope()
): ViewModel(coroutineScope) {

    class UserViewModelFactory(private val userDataSource: UserInfoRepository):
    ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(UserInputViewModel::class.java)) return UserInputViewModel(userDataSource) as T
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }

    private val _apiSuccess: MutableLiveData<UserInfoResponse> = MutableLiveData()
    val apiSuccess: LiveData<UserInfoResponse>
        get() = _apiSuccess

    private val _apiError: MutableLiveData<Boolean> = MutableLiveData()
    val apiError: LiveData<Boolean>
        get() = _apiError


    fun getUserInfo(username: String) {
        CustomIdlingResource.increment(this)
        coroutineScope.launch {
            val response = userDataSource.invoke(username)
            if (response !is NetworkResult.Success) {
                _apiError.value = true
                CustomIdlingResource.decrement(this)
            } else {
                _apiSuccess.value = response.data
                _apiError.value = false
                CustomIdlingResource.decrement(this)
            }
        }
    }

    fun onUserSaved(userInfoResponse: UserInfoResponse) {
        _apiSuccess.value = userInfoResponse
    }
}