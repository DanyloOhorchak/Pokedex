package com.example.pokedex.presentation


interface Displayable
data class PokemonItem(val id: String, val name:String, val image: String, val useColor: Boolean = false): Displayable
data class HeaderItem(val text: String): Displayable