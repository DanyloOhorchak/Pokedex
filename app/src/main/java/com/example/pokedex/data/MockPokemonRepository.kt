package com.example.pokedex.data

import com.example.pokedex.domain.RepositoryCallback
import com.example.pokedex.domain.PokemonEntity
import com.example.pokedex.domain.PokemonRepository
import io.reactivex.rxjava3.core.Single

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
    override fun getPokemonList(): Single<List<PokemonEntity>> = Single.just(items)

    override fun getPokemonById(id: String): Single<PokemonEntity> {
        val item = items.find { it.id == id }
        return if (item != null) Single.just(item) else Single.error(Throwable("No pokemon with such id"))
    }

    fun getImageByID(id: Int): String =
        "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/$id.png"
}