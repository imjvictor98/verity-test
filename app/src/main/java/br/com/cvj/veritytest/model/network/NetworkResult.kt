package br.com.cvj.veritytest.model.network

import retrofit2.HttpException
import retrofit2.Response
import timber.log.Timber


sealed class NetworkResult<T : Any> {
    class Success<T: Any>(val data: T) : NetworkResult<T>()
    class Error<T: Any>(val code: Int, val message: String?) : NetworkResult<T>()
    class Exception<T: Any>(val e: Throwable) : NetworkResult<T>()

    companion object { const val TAG = "NetworkCalls" }
}

suspend fun <T : Any> handleApi(
    execute: suspend () -> Response<T>
): NetworkResult<T> {
    return try {
        val response = execute()
        val body = response.body()

        Timber.tag(NetworkResult.TAG).d(
            "StatusCode = HTTP/S %s\nBody = %s",
            response.code(),
            response.body().toString(),
        )

        if (response.isSuccessful && body != null) {
            NetworkResult.Success(body)
        } else {
            NetworkResult.Error(code = response.code(), message = response.message())
        }

    } catch (e: HttpException) {
        Timber.tag(NetworkResult.TAG).e(e)
        NetworkResult.Error(code = e.code(), message = e.message())
    } catch (e: Throwable) {
        Timber.tag(NetworkResult.TAG).e(e)
        NetworkResult.Exception(e)
    }
}