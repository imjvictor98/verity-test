package br.com.cvj.veritytest.model.repository.user.info

import br.com.cvj.veritytest.model.network.NetworkResult
import br.com.cvj.veritytest.model.data.UserInfoResponse
import br.com.cvj.veritytest.model.network.handleApi
import br.com.cvj.veritytest.model.network.Api

class UserInfoApiDataSource: UserInfoRepository {
    override suspend fun invoke(username: String): NetworkResult<UserInfoResponse> {
        return handleApi { Api.services.getUserInfo(username) }
    }
}