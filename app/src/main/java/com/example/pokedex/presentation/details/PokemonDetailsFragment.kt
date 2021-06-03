package com.example.pokedex.presentation.details
import android.os.Bundle
import android.util.Log
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.pokedex.R
import com.example.pokedex.colorDetailsItem
import com.example.pokedex.databinding.FragmentPokemonDetailsBinding
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

const val PARAM_POKEMON_ID = "Pockemon_Id"

class PokemonDetailsFragment : Fragment(R.layout.fragment_pokemon_details) {
    private val id: String by lazy {
        arguments?.getString(PARAM_POKEMON_ID) ?: ""
    }
    val binding: FragmentPokemonDetailsBinding by viewBinding()
    private val viewModel: PokemonDetailsViewModel by viewModel { parametersOf(id) }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val id = arguments?.getString(PARAM_POKEMON_ID)

        if (id != null) {
            loadPokemonData(view)
        } else {
            Log.d("TAG", "Invalid pokemon id")
        }
    }

    private fun loadPokemonData(view: View) {
        viewModel.fetch()
        val progressBar = binding.progress
        val contentView = binding.contentGroup
        val errorView = binding.errorText
        viewModel.viewState().observe(viewLifecycleOwner, Observer {
            when (it) {
                is PokemonDetailsViewState.LoadingState -> {
                    progressBar.isVisible = true
                    contentView.isVisible = false
                    errorView.isVisible = false
                }
                is PokemonDetailsViewState.DataState -> {
                    progressBar.isVisible = false
                    contentView.isVisible = true
                    errorView.isVisible = false
                    showData(binding, view, it)
                }
                is PokemonDetailsViewState.ErrorState -> {
                    progressBar.isVisible = false
                    contentView.isVisible = false
                    errorView.isVisible = true
                }
            }
        })
    }


    private fun showData(
        binding: FragmentPokemonDetailsBinding,
        view: View,
        state: PokemonDetailsViewState.DataState
    ) {
        val nameTextView = binding.name
        val imageView = binding.image
        val abilitiesLayout = binding.abilitiesLayout
        val actionBar: ActionBar? = (activity as AppCompatActivity?)!!.supportActionBar

        nameTextView.text = state.name.capitalize()
        colorDetailsItem(nameTextView, imageView, state.url, actionBar)
        state.abilities.forEach() {
            val textView = TextView(context)
            textView.text = it.capitalize()
            val safeContext = context
            if (safeContext != null)
                textView.setTextColor(ContextCompat.getColor(safeContext, R.color.brightTextColor))
            textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, 50.0f)
            textView.setPadding(0, 10, 0, 0)
            textView.gravity = Gravity.LEFT
            abilitiesLayout.addView(textView)
        }
    }
}