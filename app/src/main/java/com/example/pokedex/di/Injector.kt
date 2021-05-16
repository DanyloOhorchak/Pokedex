package com.example.pokedex.di

import com.example.pokedex.data.NetworkPokemonRepository
import com.example.pokedex.data.createPokemonService
import com.example.pokedex.domain.PokemonRepository

object Injector {
    private val repository: PokemonRepository = NetworkPokemonRepository(
        createPokemonService()
    )
    fun getPokemonRepository() = repository
}