package com.example.pokedex.presentation.homescreen

import com.example.pokedex.presentation.PokemonItem

sealed class PokemonsListViewState {
    object LoadingState: PokemonsListViewState()
    data class ErrorState(val errorMessage:String): PokemonsListViewState()
    data class ContentState(val items: List<PokemonItem>): PokemonsListViewState()
}