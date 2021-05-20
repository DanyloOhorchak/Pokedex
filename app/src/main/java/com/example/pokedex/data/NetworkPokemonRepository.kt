package com.example.pokedex.data


import com.example.pokedex.domain.Result
import com.example.pokedex.domain.PokemonEntity
import com.example.pokedex.domain.PokemonRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class NetworkPokemonRepository(val api: PokemonService) : PokemonRepository {


    override suspend fun getPokemonList(): Result<List<PokemonEntity>> =
        withContext(Dispatchers.IO) {
            try {
                val ids = api.fetchPokemonList().results.map { it.name }
                val pokemonDetailedList = api.fetchPokemonList().results.map { it.name }
                    .map { api.fetchPokemonDetails(it).toEntity() }
                Result.Success(pokemonDetailedList)
            } catch (exception: Exception) {
                Result.Error(exception)
            }
        }


    override suspend fun getPokemonById(id: String): Result<PokemonEntity> =
        withContext(Dispatchers.IO) {
            try {
                val entity = api.fetchPokemonDetails(id).toEntity()
                Result.Success(entity)
            } catch (exception: Exception) {
                Result.Error(exception)
            }
        }

    private fun DetailedPokemonResponse.toEntity() =
        PokemonEntity(
            id = id,
            name = name,
            image = getImageById(id),
            abilities = abilities.map { it.ability.name })

    private fun getImageById(id: String): String =
        "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/$id.png"
}


