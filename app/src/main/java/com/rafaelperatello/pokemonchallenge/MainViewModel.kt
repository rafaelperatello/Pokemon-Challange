package com.rafaelperatello.pokemonchallenge

import androidx.compose.runtime.Immutable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rafaelperatello.pokemonchallenge.domain.model.shallow.ShallowPokemon
import com.rafaelperatello.pokemonchallenge.domain.repository.PokemonRepository
import com.rafaelperatello.pokemonchallenge.domain.util.DomainResult
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

internal class MainViewModel(
    private val repository: PokemonRepository
) : ViewModel() {

    val mutex = Mutex()

    private var _state: MutableStateFlow<MainViewModelState> =
        MutableStateFlow(MainViewModelState.Loading)

    val state: StateFlow<MainViewModelState>
        get() = _state.asStateFlow()

    init {
        getPokemonList(1)
    }

    fun getPokemonList(page: Int) {
        viewModelScope.launch {
            mutex.withLock {
                delay(200)
                when (val result = repository.getPokemonList(page)) {
                    is DomainResult.Success -> {
                        val pokemonList = result.data.data
                        val currentPage = result.data.page
                        val currentList =
                            (_state.value as? MainViewModelState.Success)?.pokemonList
                                ?: emptyList()
                        val newList = currentList + pokemonList
                        _state.value = MainViewModelState.Success(newList, currentPage)
                    }

                    // Todo handle different error types
                    is DomainResult.Error -> {
                        _state.value = MainViewModelState.Error
                    }
                }
            }
        }
    }

}

internal sealed class MainViewModelState {

    data object Loading : MainViewModelState()

    data object Error : MainViewModelState()

    @Immutable
    data class Success(
        val pokemonList: List<ShallowPokemon>,
        val currentPage: Int
    ) : MainViewModelState()
}
