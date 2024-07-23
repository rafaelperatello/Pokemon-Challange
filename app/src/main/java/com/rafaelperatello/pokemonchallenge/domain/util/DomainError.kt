package com.rafaelperatello.pokemonchallenge.domain.util

sealed interface DomainError

sealed interface DataError : DomainError {
    enum class Network : DataError {
        NO_INTERNET,
        BAD_REQUEST,
        UNAUTHORIZED,
        FORBIDDEN,
        NOT_FOUND,
        INTERNAL_SERVER_ERROR,
        BAD_GATEWAY,
        SERVICE_UNAVAILABLE,
        GATEWAY_TIMEOUT,
        UNKNOWN,
    }

    enum class Local : DataError {
        DISK_FULL,
    }
}
