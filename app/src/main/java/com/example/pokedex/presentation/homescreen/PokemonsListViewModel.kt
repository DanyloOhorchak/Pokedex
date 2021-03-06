package com.example.pokedex.presentation.homescreen

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pokedex.domain.PokemonRepository
import com.example.pokedex.domain.Result
import com.example.pokedex.presentation.toItem
import kotlinx.coroutines.launch

class PokemonsListViewModel(private val repository: PokemonRepository) : ViewModel() {
    private val viewStateLiveData = MutableLiveData<PokemonsListViewState>()
    fun viewState(): LiveData<PokemonsListViewState> = viewStateLiveData

    fun fetch() {
        viewStateLiveData.value = PokemonsListViewState.LoadingState

        viewModelScope.launch {
            viewStateLiveData.value = when (val result = repository.getPokemonList(0)) {
                is Result.Success -> {
                    PokemonsListViewState.ContentState(result.data.map {it.toItem()})
                }
                is Result.Error -> {
                    Log.d("ViewModel", "Error is",result.exception)
                    PokemonsListViewState.ErrorState("Failed to load data")
                }
            }
        }
    }

}
