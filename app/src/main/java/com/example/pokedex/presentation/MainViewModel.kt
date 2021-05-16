package com.example.pokedex.presentation

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.pokedex.data.NetworkPokemonRepository
import com.example.pokedex.data.createPokemonService
import com.example.pokedex.domain.RepositoryCallback
import com.example.pokedex.domain.PokemonEntity
import com.example.pokedex.domain.PokemonRepository
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.schedulers.Schedulers

class MainViewModel : ViewModel() {
    private val repository: PokemonRepository = NetworkPokemonRepository(createPokemonService())
    private var disposable: Disposable? = null
    private val _pokemonListLiveData = MutableLiveData<List<Displayable>>()
    fun getPokemonList(): LiveData<List<Displayable>> = _pokemonListLiveData


    fun loadData() {
        disposable = repository.getPokemonList()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe( {showData(it)}, { Log.d("ViewModel", "Error is", it) })
    }

    private fun showData(pokemons: List<PokemonEntity>) {


        val displayableList = pokemons.map { it.toItem() }

        _pokemonListLiveData.postValue(displayableList)
    }

    private fun PokemonEntity.toItem(): PokemonItem =
        PokemonItem(id, name, image)

    private fun List<PokemonEntity>.genFilterAndToItemConverter(generation: Int) =
        this.filter { it.generation == generation }.map { it.toItem() }
}
