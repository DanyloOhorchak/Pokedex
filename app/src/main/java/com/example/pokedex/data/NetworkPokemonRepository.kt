package com.example.pokedex.data

import android.location.GnssMeasurementsEvent
import com.example.pokedex.domain.RepositoryCallback
import com.example.pokedex.domain.PokemonEntity
import com.example.pokedex.domain.PokemonRepository
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class NetworkPokemonRepository(val api: PokemonService) : PokemonRepository {


    override fun getPokemonList(): Single<List<PokemonEntity>> = api.fetchPokemonList()
        .flatMap { pokemonList ->
            Observable.fromIterable(pokemonList.results)
                .flatMapSingle { getPokemonById(it.name) }.toList()
        }


    override fun getPokemonById(id: String): Single<PokemonEntity> = api.fetchPokemonInfo(id).map { pokemon ->
        val abilities = pokemon.abilities.map { it.ability.name }

        PokemonEntity(
            id = pokemon.id,
            name = pokemon.name,
            image = getImageById(pokemon.id),
            abilities = abilities
        )
    }

    fun getImageById(id: String): String = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/$id.png"
}


