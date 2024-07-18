package com.rafaelperatello.pokemonchallenge.domain.util

sealed class DomainResult<out R> {

    data class Success<out T>(val data: T) : DomainResult<T>()

    data class Error(val error: DomainError) : DomainResult<Nothing>()
}