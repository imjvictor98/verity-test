package br.com.cvj.veritytest.ui.user.input

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import app.cash.turbine.test
import br.com.cvj.veritytest.model.data.UserInfoResponse
import br.com.cvj.veritytest.model.network.NetworkResult
import br.com.cvj.veritytest.model.repository.user.info.UserInfoRepository
import br.com.cvj.veritytest.util.TestDispatcherProvider
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(MockitoJUnitRunner::class)
class UserInputViewModelTest {
    @get: Rule
    val rule = InstantTaskExecutorRule()

    private lateinit var testDispatcherProvider: TestDispatcherProvider

    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        testDispatcherProvider = TestDispatcherProvider()
    }

    @Test
    fun `when viewModel gets userInfo get success then sets uiState`() {
        runTest(testDispatcherProvider.main) {
            //Arrange
            val expectedUserInfo = UserInfoResponse(login = "imjvictor98")

            val expectedMockResultSuccess = NetworkResult.Success(expectedUserInfo)

            val repositoryMock = UserInfoMockRepository(expectedMockResultSuccess)

            val viewModelMock = UserInputViewModel(repositoryMock, testDispatcherProvider.main)

            val expectedUiStateSuccess = UserInputUiState.Success(expectedUserInfo)

            //Act
            viewModelMock.getUserInfo("imjvictor98")

            //Assert
            viewModelMock.uiState.test {
                assertEquals(expectedUiStateSuccess, awaitItem())
            }
        }
    }

    @Test
    fun `when viewModel gets userInfo get error then sets uiState`() {
        runTest(testDispatcherProvider.main) {
            //Arrange
            val expectedMockResultError = NetworkResult.Error<UserInfoResponse>(code = 400, null)

            val repositoryMock = UserInfoMockRepository(expectedMockResultError)

            val viewModelMock = UserInputViewModel(repositoryMock, testDispatcherProvider.main)

            val expectedUiStateError = UserInputUiState.Error(true, 400)

            //Act
            viewModelMock.getUserInfo("")

            //Assert
            viewModelMock.uiState.test {
                assertEquals(expectedUiStateError, awaitItem())
            }
        }
    }
}

class UserInfoMockRepository(
    private val result: NetworkResult<UserInfoResponse>
): UserInfoRepository {
    override suspend fun getUser(username: String) = result
}