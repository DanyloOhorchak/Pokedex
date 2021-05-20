package com.example.pokedex.presentation


import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.pokedex.presentation.homescreen.PokemonsListFragment

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportFragmentManager.beginTransaction()
            .replace(android.R.id.content ,PokemonsListFragment())
            .commit()
    }
}


