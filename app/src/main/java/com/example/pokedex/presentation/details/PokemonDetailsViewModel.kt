package com.example.pokedex.presentation.details

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.pokedex.data.NetworkPokemonRepository
import com.example.pokedex.data.createPokemonService
import com.example.pokedex.domain.PokemonEntity
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.Disposable
import java.util.concurrent.TimeUnit

class PokemonDetailsViewModel : ViewModel() {
    private val repository = NetworkPokemonRepository(createPokemonService())
    private var disposable: Disposable? = null
    private val viewStateLiveDisposable = MutableLiveData<PokemonDetailsViewState>()

    fun viewState(): LiveData<PokemonDetailsViewState> = viewStateLiveDisposable

    fun loadPokemonById(id: String) {
        viewStateLiveDisposable.value = PokemonDetailsViewState.LoadingState
        disposable = repository.getPokemonById(id)
            .delay(2, TimeUnit.SECONDS)
            .subscribeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { pokemonEntity ->
                    viewStateLiveDisposable.postValue(PokemonDetailsViewState.DataState(
                        pokemonEntity.name, pokemonEntity.image, pokemonEntity.abilities
                    ))
                },
                {
                    viewStateLiveDisposable.value =
                        PokemonDetailsViewState.ErrorState("Unable to load data about pokemon with id: $id")
                })
    }

    override fun onCleared() {
        super.onCleared()
        disposable?.dispose()
    }
}