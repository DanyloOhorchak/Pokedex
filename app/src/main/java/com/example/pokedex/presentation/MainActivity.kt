package com.example.pokedex.presentation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.pokedex.R
import com.example.pokedex.domain.PokemonEntity
import com.example.pokedex.presentation.MainAdapter
import androidx.lifecycle.Observer
import java.util.*

class MainActivity : AppCompatActivity() {
    private val viewModel: MainViewModel = MainViewModel()
    private val adapter = MainAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val recyclerView = findViewById<RecyclerView>(R.id.recycler_view)

        viewModel.getPokemonList().observe(this, Observer { pokemonList ->
            adapter.setPokemonList(pokemonList)
        })

        viewModel.loadData()
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

    }
}


