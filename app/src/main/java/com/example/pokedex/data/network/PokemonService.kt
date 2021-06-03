package com.example.pokedex.data

import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query


interface PokemonService {
    @GET("pokemon")
    suspend fun fetchPokemonList(
        @Query("limit") limit: Int = 40,
        @Query("offset") offset: Int
    ): PokemonListResponse

    @GET("pokemon/{name}")
    suspend fun fetchPokemonDetails(@Path("name") name: String): DetailedPokemonResponse
}

data class PokemonListResponse(
    val count: Int,
    val results: List<PartialPokemonResponse>
)

data class PartialPokemonResponse(
    val name: String,
    val url: String
)

data class DetailedPokemonResponse(
    val id: String,
    var name: String,
    val abilities: List<PokemonAbilities>
)

data class PokemonAbilities(
    val ability: PokemonAbilityDetailed,
    val isHidden: Boolean,
    val slot: Int
)

data class PokemonAbilityDetailed(
    val name: String,
    val url: String
)