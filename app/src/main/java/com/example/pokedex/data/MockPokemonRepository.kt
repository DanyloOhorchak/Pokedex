package com.example.pokedex.data

import com.example.pokedex.domain.Result
import com.example.pokedex.domain.PokemonEntity
import com.example.pokedex.domain.PokemonRepository
import java.lang.Exception

class MockPokemonRepository : PokemonRepository {
    val items = mutableListOf<PokemonEntity>(
        PokemonEntity("1", "bulbasaur", getImageByID(1), weight = "", height = ""),
        PokemonEntity("2", "ivysaur", getImageByID(2), weight = "", height = ""),
        PokemonEntity("3", "venusaur", getImageByID(3), 1, weight = "", height = ""),
        PokemonEntity("4", "charmander", getImageByID(4), 1, weight = "", height = ""),
        PokemonEntity("5", "charmeleon", getImageByID(5), 2, weight = "", height = ""),
        PokemonEntity("6", "charizard", getImageByID(6), 2, weight = "", height = ""),
        PokemonEntity("7", "squirtle", getImageByID(7), 2, weight = "", height = ""),
        PokemonEntity("8", "wartortle", getImageByID(8), 3, weight = "", height = ""),
        PokemonEntity("9", "blastoise", getImageByID(9), 3, weight = "", height = ""),
        PokemonEntity("10", "caterpie", getImageByID(10), 3, weight = "", height = "")
    )
    override suspend fun getPokemonList(offset: Int): Result<List<PokemonEntity>> = Result.Success(items)

    override suspend fun getPokemonById(id: String): Result<PokemonEntity> {
        val item = items.find { it.id == id }
        return if (item != null) Result.Success(item) else Result.Error(Exception("No pokemon with such id"))
    }
    fun getImageByID(id: Int): String =
        "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/$id.png"
}