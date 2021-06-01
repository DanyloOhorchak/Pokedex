package com.example.pokedex.presentation.details

import android.graphics.Bitmap
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.palette.graphics.Palette
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.BitmapImageViewTarget
import com.bumptech.glide.request.transition.Transition
import com.example.pokedex.R
import com.example.pokedex.presentation.adapter.StylingInstrumens
import com.example.pokedex.presentation.adapter.StylingInstrumens.darkenColor
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class PokemonDetailsFragment : Fragment(R.layout.fragment_pokemon_details) {
    private val id: String by lazy {
        arguments?.getString(PARAM_POKEMON_ID) ?: ""
    }
    private val viewModel:PokemonDetailsViewModel by viewModel{ parametersOf(id)}


    companion object {
        private const val PARAM_POKEMON_ID = "Pockemon_Id"


        fun newInstance(id: String): Fragment = PokemonDetailsFragment().apply {
            arguments = bundleOf(
                PARAM_POKEMON_ID to id
            )
        }
    }

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

        val progressBar = view.findViewById<ProgressBar>(R.id.progress)
        val contentView = view.findViewById<View>(R.id.content_group)
        val errorView = view.findViewById<TextView>(R.id.error_text)

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
                    showData(view,it)
                }
                is PokemonDetailsViewState.ErrorState -> {
                    progressBar?.isVisible = false
                    contentView?.isVisible = false
                    errorView?.isVisible = true
                }
            }
        })
    }


    private fun showData(view: View, state: PokemonDetailsViewState.DataState) {
        val nameTextView = view.findViewById<TextView>(R.id.name)
        val imageView = view.findViewById<ImageView>(R.id.image)
        val abilitiesLayout = view.findViewById<LinearLayout>(R.id.abilitiesLayout)
        val actionBar: ActionBar? = (activity as AppCompatActivity?)!!.supportActionBar


        nameTextView.text = state.name.capitalize()

        Glide.with(imageView.context)
            .asBitmap()
            .load(state.url)
            .into(object : BitmapImageViewTarget(imageView) {
                override fun onResourceReady(
                    resource: Bitmap,
                    transition: Transition<in Bitmap>?
                ) {
                    super.onResourceReady(resource, transition)
                    Palette.from(resource).generate { palette ->
                        val swatch = palette?.vibrantSwatch
                        var rgbColor = ContextCompat.getColor(imageView.context, R.color.teal_main)
                        if (swatch != null) {
                            rgbColor = swatch.rgb
                        }
                        imageView.setBackgroundColor(darkenColor(rgbColor))
                        actionBar?.setBackgroundDrawable(ColorDrawable(rgbColor))
                        if (StylingInstrumens.isBrightColor(rgbColor)) {
                            nameTextView.setTextColor(
                                ContextCompat.getColor(
                                    nameTextView.context,
                                    R.color.darkTextColor
                                )
                            )
                        } else {
                            nameTextView.setTextColor(
                                ContextCompat.getColor(
                                    nameTextView.context,
                                    R.color.brightTextColor
                                )
                            )
                        }
                    }
                }
            })


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