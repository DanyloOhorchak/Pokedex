package com.example.pokedex.domain

data class PokemonEntity(
    val id: String,
    val name: String,
    val image: String,
    val generation: Int = 0,
    val abilities: List<String> = emptyList()
)
