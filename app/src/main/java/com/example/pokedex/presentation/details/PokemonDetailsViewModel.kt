package com.example.pokedex.presentation.details

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pokedex.data.NetworkPokemonRepository
import com.example.pokedex.data.createPokemonService
import com.example.pokedex.domain.PokemonEntity
import com.example.pokedex.domain.Result
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class PokemonDetailsViewModel : ViewModel() {
    private val repository = NetworkPokemonRepository(createPokemonService())
    private val viewStateLiveData = MutableLiveData<PokemonDetailsViewState>()

    fun viewState(): LiveData<PokemonDetailsViewState> = viewStateLiveData

    fun loadPokemonById(id: String) {
        viewStateLiveData.value = PokemonDetailsViewState.LoadingState

        viewModelScope.launch {
            delay(2000)
            viewStateLiveData.value = when (val result = repository.getPokemonById(id)) {
                is Result.Success -> {
                    result.data.toDataViewState()
                }
                is Result.Error -> {
                    PokemonDetailsViewState.ErrorState("Unable to load data about pokemon with id: $id")
                }
            }
        }
    }

    fun PokemonEntity.toDataViewState() = PokemonDetailsViewState.DataState(
        name = name,
        url = image,
        abilities = abilities
    )
}