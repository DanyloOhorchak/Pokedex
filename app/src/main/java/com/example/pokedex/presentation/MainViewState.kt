package com.example.pokedex.presentation

sealed class MainViewState {
    object LoadingState: MainViewState()
    data class ErrorState(val errorMessage:String): MainViewState()
    data class ContentState(val items: List<PokemonItem>): MainViewState()
}