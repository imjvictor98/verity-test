package br.com.cvj.veritytest.model.repository.user.profile

import br.com.cvj.veritytest.model.data.UserRepositoryItemResponse
import br.com.cvj.veritytest.model.network.Api
import br.com.cvj.veritytest.model.network.NetworkResult
import br.com.cvj.veritytest.model.network.handleApi

class UserProfileDataSource: UserProfileRepository {
    override suspend fun getRepositories(username: String): NetworkResult<List<UserRepositoryItemResponse>> {
        return handleApi { Api.services.getUserRepos(username) }
    }
}