package com.example.pokedex.presentation

import androidx.lifecycle.GeneratedAdapter
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.pokedex.data.MockPokemonRepository
import com.example.pokedex.presentation.Displayable
import com.example.pokedex.domain.PokemonEntity
import com.example.pokedex.domain.PokemonRepository

class MainViewModel : ViewModel() {
    private val repository: PokemonRepository = MockPokemonRepository()

    private val _pokemonListLiveData = MutableLiveData<List<Displayable>>()

    fun getPokemonList(): LiveData<List<Displayable>> = _pokemonListLiveData

    fun loadData() {
        val pokemons = repository.getPokemonList()

        val gen0 = pokemons.genFilterAndToItemConverter(0)
        val gen1 = pokemons.genFilterAndToItemConverter(1)
        val gen2 = pokemons.genFilterAndToItemConverter(2)
        val gen3 = pokemons.genFilterAndToItemConverter(3)

        val displayableList =  mutableListOf<Displayable>()

        displayableList.add(HeaderItem("GENERATION ZER0"))
        displayableList.addAll(gen0)
        displayableList.add(HeaderItem("GENERATION ONE"))
        displayableList.addAll(gen1)
        displayableList.add(HeaderItem("GENERATION TWO"))
        displayableList.addAll(gen2)
        displayableList.add(HeaderItem("GENERATION THREE"))
        displayableList.addAll(gen3)

        _pokemonListLiveData.value = displayableList
    }

    private fun PokemonEntity.toItem(): PokemonItem =
        PokemonItem(id, name, image)

    private fun List<PokemonEntity>.genFilterAndToItemConverter(generation: Int) = this.filter { it.generation == generation }.map { it.toItem() }
}
