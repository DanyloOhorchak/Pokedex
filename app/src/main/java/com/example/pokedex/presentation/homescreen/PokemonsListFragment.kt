package com.example.pokedex.presentation.homescreen

import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import com.example.pokedex.R
import com.example.pokedex.presentation.Displayable
import com.example.pokedex.presentation.adapter.PokemonsListAdapter
import com.example.pokedex.presentation.details.PARAM_POKEMON_ID
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import org.koin.androidx.viewmodel.ext.android.viewModel
import com.example.pokedex.databinding.FragmentPokemonsListBinding

class PokemonsListFragment : Fragment(R.layout.fragment_pokemons_list) {
    private val viewModel: PokemonsListViewModel by viewModel()
    private var pokemonsListAdapter: PokemonsListAdapter? = null
    private val binding: FragmentPokemonsListBinding by viewBinding()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as AppCompatActivity?)!!.supportActionBar?.setBackgroundDrawable(ColorDrawable(
            ContextCompat.getColor(
                view.context,
                R.color.teal_main
            )))
        createRecyclerView(binding)
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


    private fun createRecyclerView(binding: FragmentPokemonsListBinding) {
        pokemonsListAdapter = PokemonsListAdapter(
            onItemClicked = { id ->
                val bundle = bundleOf(
                    PARAM_POKEMON_ID to id
                )
                findNavController().navigate(R.id.action_pokemonList_to_pokemonDetails, bundle)
            }
        )
        binding.recyclerView.apply {
            layoutManager = GridLayoutManager(context, 2)
            adapter = pokemonsListAdapter
        }
    }

    private fun showProgress() {
        Toast.makeText(context, "Loading...", Toast.LENGTH_LONG).show()
    }

    private fun showData(items: List<Displayable>) {
        pokemonsListAdapter?.setPokemonList(items)
    }

    private fun showError(errorMessage: String) {
        Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show()
    }
}