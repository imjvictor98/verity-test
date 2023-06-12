package br.com.cvj.veritytest.ui.user.profile

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import app.cash.turbine.test
import br.com.cvj.veritytest.model.data.UserRepositoryItemResponse
import br.com.cvj.veritytest.model.network.NetworkResult
import br.com.cvj.veritytest.model.repository.user.profile.UserProfileRepository
import br.com.cvj.veritytest.util.TestDispatcherProvider
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
@OptIn(ExperimentalCoroutinesApi::class)
class UserProfileViewModelTest {
    @get: Rule
    val rule = InstantTaskExecutorRule()

    private lateinit var testDispatcherProvider: TestDispatcherProvider

    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        testDispatcherProvider = TestDispatcherProvider()
    }

    @Test
    fun `when viewModel gets userRepos get success then sets uiState`() {
        runTest(testDispatcherProvider.main) {
            //Arrange
            val expectedUserRepos = emptyList<UserRepositoryItemResponse>()

            val expectedMockResultSuccess = NetworkResult.Success(expectedUserRepos)

            val repositoryMock = UserProfileMockRepository(expectedMockResultSuccess)

            val viewModelMock = UserProfileViewModel(repositoryMock, testDispatcherProvider.main)

            val expectedUiStateSuccess = UserProfileUiState.Reload(true)

            //Act
            viewModelMock.getUserRepositories("imjvictor98")

            //Assert
            viewModelMock.uiState.test {
                Assert.assertEquals(expectedUiStateSuccess, awaitItem())
            }
        }
    }

    @Test
    fun `when viewModel gets userRepos get error then sets uiState`() {
        runTest(testDispatcherProvider.main) {
            //Arrange
            val expectedMockResultSuccess = NetworkResult.Error<List<UserRepositoryItemResponse>>(400, null)

            val repositoryMock = UserProfileMockRepository(expectedMockResultSuccess)

            val viewModelMock = UserProfileViewModel(repositoryMock, testDispatcherProvider.main)

            val expectedUiStateSuccess = UserProfileUiState.Reload(false)

            //Act
            viewModelMock.getUserRepositories("")

            //Assert
            viewModelMock.uiState.test {
                Assert.assertEquals(expectedUiStateSuccess, awaitItem())
            }
        }
    }
}

class UserProfileMockRepository(
    private val result: NetworkResult<List<UserRepositoryItemResponse>>
): UserProfileRepository {
    override suspend fun getRepositories(username: String) = result
}