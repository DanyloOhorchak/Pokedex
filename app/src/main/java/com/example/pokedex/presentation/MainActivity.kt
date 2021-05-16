package com.example.pokedex.presentation


import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.pokedex.R
import com.example.pokedex.presentation.adapter.MainAdapter
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import com.example.pokedex.presentation.details.PokemonDetailsActivity

class MainActivity : AppCompatActivity() {
    private val viewModel: MainViewModel = MainViewModel()
    private var adapter: MainAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        createRecyclerView()
        viewModel.viewState().observe(this, Observer {
            when(it) {
                is MainViewState.LoadingState -> {
                    showProgress()
                }

                is MainViewState.ErrorState -> {
                    showError("Unable to load data")
                }
                is MainViewState.ContentState -> {
                    showData(it.items)
                }
            }
        })
        viewModel.loadData()
    }

    private fun createRecyclerView() {
        adapter = MainAdapter(
            onItemClicked = { id ->
                startActivity(PokemonDetailsActivity.intent(this,id))
            }
        )
        val recyclerView = findViewById<RecyclerView>(R.id.recycler_view)
        recyclerView.layoutManager = GridLayoutManager(this,2,GridLayoutManager.VERTICAL, false)
        recyclerView.adapter = adapter
    }

    private fun showProgress() {
        Toast.makeText(this,"Loading...",Toast.LENGTH_LONG).show()
    }

    private fun showData(items: List<Displayable>) {
        adapter?.setPokemonList(items)
    }

    private fun showError(errorMessage: String) {
        Toast.makeText(this,errorMessage,Toast.LENGTH_SHORT).show()
    }

}


