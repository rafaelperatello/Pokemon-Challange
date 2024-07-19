package com.rafaelperatello.pokemonchallenge.ui.screen.home

import android.util.Log
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

    private val mutex = Mutex()

    private var _state: MutableStateFlow<MainViewModelState> =
        MutableStateFlow(MainViewModelState.Loading)

    val state: StateFlow<MainViewModelState>
        get() = _state.asStateFlow()

    private var _listState: MutableStateFlow<ListState> = MutableStateFlow(ListState.IDLE)
    val listState: StateFlow<ListState>
        get() = _listState.asStateFlow()

    init {
        fetchInitialPage()
    }

    private fun fetchInitialPage() {
        viewModelScope.launch {
            mutex.withLock {
                delay(200) // Todo remove this delay

                val result = repository.getPokemonList(1)
                Log.d("MainViewModel", "fetchInitialPage result: $result")

                when (result) {
                    is DomainResult.Success -> {
                        val pokemonList = result.data.data
                        val currentPage = result.data.page
                        _state.value = MainViewModelState.Success(pokemonList, currentPage)
                    }

                    is DomainResult.Error -> {
                        // Todo handle different error types
                        _state.value = MainViewModelState.Error
                    }
                }
            }
        }
    }

    fun onRetryClick() {
        if (_state.value is MainViewModelState.Error) {
            _state.value = MainViewModelState.Loading
            fetchInitialPage()
        }
    }

    fun fetchNextPage() {
        val currentListState = _listState.value
        Log.d("MainViewModel", "fetchNextPage currentListState: $currentListState")

        if (currentListState != ListState.IDLE && currentListState != ListState.ERROR) return

        viewModelScope.launch {
            mutex.withLock {
                val localState = _state.value as? MainViewModelState.Success ?: return@withLock
                val nextPage = localState.currentPage + 1

                _listState.value = ListState.PAGINATING

                delay(2000) // Todo remove this delay

                val result = repository.getPokemonList(nextPage)
                Log.d("MainViewModel", "fetchNextPage result: $result")

                when (result) {
                    is DomainResult.Success -> {
                        val pokemonList = result.data.data
                        val newCurrentPage = result.data.page
                        val newList = localState.pokemonList + pokemonList
                        _state.value = MainViewModelState.Success(newList, newCurrentPage)

                        _listState.value =
                            if (newList.size < result.data.totalCount) ListState.IDLE
                            else ListState.PAGINATION_EXHAUST
                    }

                    is DomainResult.Error -> {
                        _listState.value = ListState.ERROR
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

internal enum class ListState {
    IDLE,
    PAGINATING,
    ERROR,
    PAGINATION_EXHAUST
}