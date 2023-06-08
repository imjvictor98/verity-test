package br.com.cvj.veritytest.ui.user.input

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import br.com.cvj.veritytest.model.data.UserInfoResponse
import br.com.cvj.veritytest.model.network.NetworkResult
import br.com.cvj.veritytest.model.repository.user.info.UserInfoRepository
import br.com.cvj.veritytest.util.CloseableCoroutineScope
import br.com.cvj.veritytest.util.MoshiUtil
import com.nhaarman.mockitokotlin2.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
@OptIn(ExperimentalCoroutinesApi::class)
class UserInputViewModelTest {
    @get: Rule
    val rule = InstantTaskExecutorRule()

    private val testDispatcher: TestDispatcher = UnconfinedTestDispatcher()

    @Mock
    private lateinit var apiSuccessObserver: Observer<UserInfoResponse>

    @Mock
    private lateinit var apiErrorObserver: Observer<Boolean>

    @Mock
    private lateinit var moshiUtil: MoshiUtil

    private lateinit var viewModel: UserInputViewModel

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
    }

    @Test
    fun `when viewModel gets userInfo get success then sets apiSuccessObserver`() {
        // Arrange
        val userInfo = moshiUtil.userInfoFromJson

        val resultSuccess = UserMockRepository(NetworkResult.Success(data = userInfo))

        viewModel = UserInputViewModel(resultSuccess, CloseableCoroutineScope(testDispatcher))

        viewModel.apiSuccess.observeForever(apiSuccessObserver)

        viewModel.apiError.observeForever(apiErrorObserver)

        // Act
        viewModel.getUserInfo("imjvictor98")

        // Assert
        verify(apiSuccessObserver).onChanged(userInfo)
        verify(apiErrorObserver).onChanged(false)
    }
}

class UserMockRepository(
    private val result: NetworkResult<UserInfoResponse>
): UserInfoRepository {
    override suspend fun invoke(username: String) = result
}