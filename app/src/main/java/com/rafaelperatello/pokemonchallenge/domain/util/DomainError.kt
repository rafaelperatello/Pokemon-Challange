package com.rafaelperatello.pokemonchallenge.domain.util

sealed interface DomainError

sealed interface DataError : DomainError {

    enum class Network : DataError {
        REQUEST_TIMEOUT,
        TOO_MANY_REQUESTS,
        NO_INTERNET,
        PAYLOAD_TOO_LARGE,
        SERVER_ERROR,
        SERIALIZATION,
        UNKNOWN
    }

    enum class Local : DataError {
        DISK_FULL
    }
}