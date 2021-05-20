package com.example.pokedex.data

import com.example.pokedex.domain.Result
import com.example.pokedex.domain.PokemonEntity
import com.example.pokedex.domain.PokemonRepository
import java.lang.Exception

class MockPokemonRepository : PokemonRepository {
    val items = mutableListOf<PokemonEntity>(
        PokemonEntity("1", "bulbasaur", getImageByID(1)),
        PokemonEntity("2", "ivysaur", getImageByID(2)),
        PokemonEntity("3", "venusaur", getImageByID(3), 1),
        PokemonEntity("4", "charmander", getImageByID(4), 1),
        PokemonEntity("5", "charmeleon", getImageByID(5), 2),
        PokemonEntity("6", "charizard", getImageByID(6), 2),
        PokemonEntity("7", "squirtle", getImageByID(7), 2),
        PokemonEntity("8", "wartortle", getImageByID(8), 3),
        PokemonEntity("9", "blastoise", getImageByID(9), 3),
        PokemonEntity("10", "caterpie", getImageByID(10), 3)
    )
    override suspend fun getPokemonList(): Result<List<PokemonEntity>> = Result.Success(items)

    override suspend fun getPokemonById(id: String): Result<PokemonEntity> {
        val item = items.find { it.id == id }
        return if (item != null) Result.Success(item) else Result.Error(Exception("No pokemon with such id"))
    }
    fun getImageByID(id: Int): String =
        "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/$id.png"
}