package com.rafaelperatello.pokemonchallenge.data.repository.remote.util

import android.util.Log
import com.rafaelperatello.pokemonchallenge.domain.util.DataError.Network
import com.rafaelperatello.pokemonchallenge.domain.util.DomainResult
import retrofit2.Response

inline fun <DTO, ENTITY> safeApiCall(
    mapper: (DTO) -> ENTITY,
    apiCall: () -> Response<DTO>,
): DomainResult<ENTITY> {
    try {
        val response = apiCall()

        Log.i("Cache info", response.raw().cacheResponse.toString())

        if (response.isSuccessful) {
            val body = response.body()
            body?.let {
                return DomainResult.Success(mapper(body))
            }
        }

        val errorBody = response.errorBody()?.string()
        val responseCode = response.code()
        val errorMessage = response.message()

        Log.i(
            "Retrofit error",
            "Error code: $responseCode, Error message: $errorMessage, Error body: $errorBody",
        )

        val error =
            when (responseCode) {
                400 -> Network.BAD_REQUEST
                401 -> Network.UNAUTHORIZED
                403 -> Network.FORBIDDEN
                404 -> Network.NOT_FOUND
                500 -> Network.INTERNAL_SERVER_ERROR
                502 -> Network.BAD_GATEWAY
                503 -> Network.SERVICE_UNAVAILABLE
                504 -> Network.GATEWAY_TIMEOUT
                else -> Network.UNKNOWN
            }

        return DomainResult.Error(error)
    } catch (e: Exception) {
        Log.e("Retrofit error", e.toString())

        val networkError =
            when (e) {
                is java.net.UnknownHostException -> Network.NO_INTERNET
                is java.net.SocketTimeoutException -> Network.GATEWAY_TIMEOUT
                else -> Network.UNKNOWN
            }
        return DomainResult.Error(networkError)
    }
}
