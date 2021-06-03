package com.example.pokedex.domain


interface PokemonRepository {
    suspend fun getPokemonList(offset: Int): Result<List<PokemonEntity>>
    suspend fun getPokemonById(id: String):Result<PokemonEntity>
}