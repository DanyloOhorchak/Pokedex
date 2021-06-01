package com.example.pokedex.di

import com.example.pokedex.data.NetworkPokemonRepository
import com.example.pokedex.data.PokemonService
import com.example.pokedex.domain.PokemonRepository
import com.example.pokedex.presentation.details.PokemonDetailsViewModel
import com.example.pokedex.presentation.homescreen.PokemonsListViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

val appModule = module {
    single { createPokemonService() }
    single<PokemonRepository> { NetworkPokemonRepository(get()) }

    viewModel {PokemonsListViewModel(get())}
    viewModel { (id: String) -> PokemonDetailsViewModel(get(), id)}
}

private fun createPokemonService(): PokemonService {
    val retrofit = Retrofit.Builder()
        .baseUrl("https://pokeapi.co/api/v2/")
        .addConverterFactory(MoshiConverterFactory.create())
        .build()
    return retrofit.create(PokemonService::class.java)
}