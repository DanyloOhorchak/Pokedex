package com.example.pokedex.presentation.details

sealed class PokemonDetailsViewState {
    object LoadingState: PokemonDetailsViewState()
    data class DataState(val name: String,val url: String, val abilities: List<String>): PokemonDetailsViewState()
    data class ErrorState(val message: String): PokemonDetailsViewState()
}