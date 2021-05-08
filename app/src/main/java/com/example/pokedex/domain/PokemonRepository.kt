package com.example.pokedex.domain

import com.example.pokedex.domain.PokemonEntity

interface PokemonRepository {
    fun getPokemonList():List<PokemonEntity>
    fun addNewPokemon(pokemon: PokemonEntity)
}