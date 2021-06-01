package com.example.pokedex.presentation.homescreen

import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.pokedex.R
import com.example.pokedex.presentation.Displayable
import com.example.pokedex.presentation.adapter.PokemonsListAdapter
import com.example.pokedex.presentation.details.PokemonDetailsFragment
import org.koin.androidx.viewmodel.ext.android.viewModel

class PokemonsListFragment : Fragment(R.layout.fragment_pokemons_list) {
    private val viewModel: PokemonsListViewModel by viewModel()
    private var adapter: PokemonsListAdapter? = null


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as AppCompatActivity?)!!.supportActionBar?.setBackgroundDrawable(ColorDrawable(
            ContextCompat.getColor(
            view.context,
            R.color.teal_main
        )))
        createRecyclerView()
        viewModel.viewState().observe(viewLifecycleOwner, Observer {
            when (it) {
                is PokemonsListViewState.LoadingState -> {
                    showProgress()
                }

                is PokemonsListViewState.ErrorState -> {
                    showError("Unable to load data")
                }
                is PokemonsListViewState.ContentState -> {
                    showData(it.items)
                }
            }
        })
        viewModel.fetch()
    }


    private fun createRecyclerView() {
        adapter = PokemonsListAdapter(
            onItemClicked = { id ->
                val safeContext = context
                if (safeContext != null)
                    activity?.let {
                        it.supportFragmentManager.beginTransaction()
                            .replace(android.R.id.content, PokemonDetailsFragment.newInstance(id))
                            .addToBackStack(null)
                            .commit()
                    }
            }
        )
        val recyclerView = view?.findViewById<RecyclerView>(R.id.recycler_view)
        recyclerView?.layoutManager =
            GridLayoutManager(context, 2)
        recyclerView?.adapter = adapter
    }

    private fun showProgress() {
        Toast.makeText(context, "Loading...", Toast.LENGTH_LONG).show()
    }

    private fun showData(items: List<Displayable>) {
        adapter?.setPokemonList(items)
    }

    private fun showError(errorMessage: String) {
        Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show()
    }
}