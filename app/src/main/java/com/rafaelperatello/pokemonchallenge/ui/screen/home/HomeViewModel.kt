package com.rafaelperatello.pokemonchallenge.ui.screen.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.rafaelperatello.pokemonchallenge.domain.model.shallow.ShallowPokemon
import com.rafaelperatello.pokemonchallenge.domain.repository.PokemonRepository
import com.rafaelperatello.pokemonchallenge.domain.settings.AppSettings
import com.rafaelperatello.pokemonchallenge.domain.settings.GridStyle
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

internal class MainViewModel(
    private val repository: PokemonRepository,
    private val appSettings: AppSettings
) : ViewModel() {

    private val _listState: MutableStateFlow<PagingData<ShallowPokemon>> =
        MutableStateFlow(value = PagingData.empty())
    val listState: StateFlow<PagingData<ShallowPokemon>> get() = _listState

    val gridStyle: StateFlow<GridStyle> = appSettings.gridStyle.stateIn(
        viewModelScope,
        started = SharingStarted.Eagerly,
        initialValue = GridStyle.SMALL
    )

    init {
        fetchData()
    }

    private fun fetchData() = viewModelScope.launch {
        viewModelScope.launch {
            repository.getShallowPokemonList()
                .distinctUntilChanged()
                .cachedIn(viewModelScope)
                .collect {
                    Log.d("PokemonPaging", "VM - fetchData: $it")
                    _listState.value = it
                }
        }
    }

    fun onGridStyleUpdated(style: GridStyle) {
        viewModelScope.launch {
            appSettings.setGridStyle(style)
        }
    }
}
