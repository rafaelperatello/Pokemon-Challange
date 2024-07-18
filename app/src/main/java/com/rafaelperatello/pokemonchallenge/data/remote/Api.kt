package com.rafaelperatello.pokemonchallenge.data.remote

import com.rafaelperatello.pokemonchallenge.domain.util.DataError.Network
import com.rafaelperatello.pokemonchallenge.domain.util.DomainResult
import retrofit2.Response

object Api {

    const val BASE_URL: String = "https://api.pokemontcg.io/v2/"
}

suspend inline fun <DTO, DOMAIN> safeApiCall(
    mapper: (DTO) -> DOMAIN,
    apiCall: suspend () -> Response<DTO>
): DomainResult<DOMAIN> {
    try {
        val response = apiCall()
        if (response.isSuccessful) {
            val body = response.body()
            body?.let {
                return DomainResult.Success(mapper(body))
            }
        }

        // Todo handle error response
        // https://github.com/philipplackner/CleanErrorHandling/blob/master/app/src/main/java/com/plcoding/cleanerrorhandling/data/AuthRepositoryImpl.kt
        val errorBody = response.errorBody()?.string()
        val responseCode = response.code()
        val errorMessage = response.message()

        return DomainResult.Error(Network.UNKNOWN)
    } catch (e: Exception) {
        return DomainResult.Error(Network.UNKNOWN)
    }
}