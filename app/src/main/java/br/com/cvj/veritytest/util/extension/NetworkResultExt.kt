package br.com.cvj.veritytest.util.extension

import br.com.cvj.veritytest.model.network.NetworkResult

fun <T : Any> NetworkResult<T>.onSuccess(
    executable: (T) -> Unit
): NetworkResult<T> = apply {
    if (this is NetworkResult.Success<T>) {
        executable(data)
    }
}

fun <T : Any> NetworkResult<T>.onError(
    executable: (code: Int, message: String?) -> Unit
): NetworkResult<T> = apply {
    if (this is NetworkResult.Error<T>) {
        executable(code, message)
    }
}

fun <T : Any> NetworkResult<T>.onException(
    executable: (e: Throwable) -> Unit
): NetworkResult<T> = apply {
    if (this is NetworkResult.Exception<T>) {
        executable(e)
    }
}