package com.example.pokedex.domain

import io.reactivex.rxjava3.core.Single

interface PokemonRepository {
    fun getPokemonList(): Single<List<PokemonEntity>>
    fun getPokemonById(id: String):Single<PokemonEntity>
}

interface RepositoryCallback<T> {
    fun onSuccess(data: T)
    fun onError(error: String)
}