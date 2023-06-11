package br.com.cvj.veritytest.model.repository.user.profile

import br.com.cvj.veritytest.model.data.UserRepositoryItemResponse
import br.com.cvj.veritytest.model.network.NetworkResult

interface UserProfileRepository {
    suspend fun getRepositories(username: String): NetworkResult<List<UserRepositoryItemResponse>>
}