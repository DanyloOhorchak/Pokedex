package com.example.pokedex.data

import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query


fun createPokemonService(): PokemonService {
    val retrofit = Retrofit.Builder()
        .baseUrl("https://pokeapi.co/api/v2/")
        .addConverterFactory(MoshiConverterFactory.create())
        .build()
    return retrofit.create(PokemonService::class.java)
}

interface PokemonService {

    @GET("pokemon")
    suspend fun fetchPokemonList(
        @Query("limit") limit: Int = 25,
        @Query("offset") offset: Int = 0
    ): PokemonListResponse

    @GET("pokemon/{name}")
//    fun fetchPokemonInfo(@Path("name") name: String): Single<DetailedPokemonResponse>
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