package br.com.cvj.veritytest.model.network

import br.com.cvj.veritytest.model.data.UserInfoResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface Services {
    @GET("/users/{username}")
    suspend fun getUserInfo(@Path("username") username: String): Response<UserInfoResponse>
}