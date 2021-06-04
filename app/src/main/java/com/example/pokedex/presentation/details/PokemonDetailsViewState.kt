package com.example.pokedex.presentation.details

sealed class PokemonDetailsViewState {
    object LoadingState: PokemonDetailsViewState()
    data class DataState(val name: String,val url: String, val abilities: List<String>, val stats:Map<String,String>, val types: List<String>, val weight: String, val height: String): PokemonDetailsViewState()
    data class ErrorState(val message: String): PokemonDetailsViewState()
}