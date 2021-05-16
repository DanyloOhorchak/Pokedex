package com.example.pokedex.presentation.details

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.core.view.marginTop
import androidx.lifecycle.Observer
import androidx.palette.graphics.Palette
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.BitmapImageViewTarget
import com.bumptech.glide.request.transition.Transition
import com.example.pokedex.R
import com.example.pokedex.presentation.adapter.StylingInstrumens
import com.example.pokedex.presentation.adapter.StylingInstrumens.darkenColor

class PokemonDetailsActivity : AppCompatActivity() {
    private val viewModel = PokemonDetailsViewModel()

    companion object {
        private const val PARAM_POKEMON_ID = "Pockemon_Id"

        fun intent(context: Context, id: String): Intent {
            val intent = Intent(context, PokemonDetailsActivity::class.java)
            return intent.putExtra(PARAM_POKEMON_ID, id)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pokemon_details)

        val id = intent.extras?.getString(PARAM_POKEMON_ID)

        if(id != null) {
            loadPokemonData(id)
        } else {
            Log.d("TAG","Invalid pokemon id")
            finish()
        }
    }

    private fun loadPokemonData(id: String) {
        viewModel.loadPokemonById(id)

        val progressBar = findViewById<ProgressBar>(R.id.progress)
        val contentView = findViewById<View>(R.id.content_group)
        val errorView = findViewById<TextView>(R.id.error_text)

        viewModel.viewState().observe(this, Observer {
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
                    showData(it)
                }
                is PokemonDetailsViewState.ErrorState -> {
                    progressBar.isVisible = false
                    contentView.isVisible = false
                    errorView.isVisible = true
                }
            }
        })
    }


    private fun showData(state: PokemonDetailsViewState.DataState) {
        val nameTextView = findViewById<TextView>(R.id.name)
        val imageView = findViewById<ImageView>(R.id.image)
        val abilitiesLayout = findViewById<LinearLayout>(R.id.abilitiesLayout)


        nameTextView.text = state.name
        Glide.with(imageView.context).load(state.url).into(imageView)

        Glide.with(imageView.context)
            .asBitmap()
            .load(state.url)
            .into(object : BitmapImageViewTarget(imageView) {
                override fun onResourceReady(
                    resource: Bitmap,
                    transition: Transition<in Bitmap>?
                ) {
                    super.onResourceReady(resource, transition)
                    Palette.generateAsync(resource, object : Palette.PaletteAsyncListener {
                        override fun onGenerated(palette: Palette?) {
                            val swatch = if(palette != null) palette.vibrantSwatch else Palette.Swatch(1,1)
                            imageView.setBackgroundColor(darkenColor(swatch!!.rgb))
                            supportActionBar?.setBackgroundDrawable(ColorDrawable(swatch!!.rgb))
                            if(StylingInstrumens.isBrightColor(swatch!!.rgb)) {
                                nameTextView.setTextColor(ContextCompat.getColor(nameTextView.context,R.color.darkTextColor))
                            } else {
                                nameTextView.setTextColor(ContextCompat.getColor(nameTextView.context,R.color.brightTextColor))
                            }
                        }
                    })
                }
            })


        state.abilities.forEach {
            val textView = TextView(this)
            textView.text = it
            textView.setTextColor(ContextCompat.getColor(this,R.color.brightTextColor))
            textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, 50.0f)
            textView.setPadding(0,10,0,0)
            textView.gravity = Gravity.LEFT
            abilitiesLayout.addView(textView)
        }

    }
}