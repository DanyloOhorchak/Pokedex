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
        val gen0 = pokemons.genFilterAndToItemConverter(0)
        val gen1 = pokemons.genFilterAndToItemConverter(1)
        val gen2 = pokemons.genFilterAndToItemConverter(2)
        val gen3 = pokemons.genFilterAndToItemConverter(3)

        val displayableList = mutableListOf<Displayable>()

        displayableList.add(HeaderItem("GENERATION ZER0"))
        displayableList.addAll(gen0)
        displayableList.add(HeaderItem("GENERATION ONE"))
        displayableList.addAll(gen1)
        displayableList.add(HeaderItem("GENERATION TWO"))
        displayableList.addAll(gen2)
        displayableList.add(HeaderItem("GENERATION THREE"))
        displayableList.addAll(gen3)

        _pokemonListLiveData.postValue(displayableList)
    }

    private fun PokemonEntity.toItem(): PokemonItem =
        PokemonItem(id, name, image)

    private fun List<PokemonEntity>.genFilterAndToItemConverter(generation: Int) =
        this.filter { it.generation == generation }.map { it.toItem() }
}
