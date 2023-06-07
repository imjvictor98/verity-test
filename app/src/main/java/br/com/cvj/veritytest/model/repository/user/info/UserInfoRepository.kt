package br.com.cvj.veritytest.model.repository.user.info

import br.com.cvj.veritytest.model.network.NetworkResult
import br.com.cvj.veritytest.model.data.UserInfoResponse

interface UserInfoRepository {
    suspend operator fun invoke(username: String): NetworkResult<UserInfoResponse>
}