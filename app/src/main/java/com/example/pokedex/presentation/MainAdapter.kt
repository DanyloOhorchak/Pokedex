package com.example.pokedex.presentation

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.ColorInt
import androidx.cardview.widget.CardView
import androidx.palette.graphics.Palette
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.FutureTarget
import com.bumptech.glide.request.target.BitmapImageViewTarget
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.example.pokedex.R
import java.net.URI
import java.net.URL


private const val ITEM_TYPE_UNKNOWN = 0
private const val ITEM_TYPE_POKEMON = 1
private const val ITEM_TYPE_HEADER = 2

class MainAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var items: MutableList<Displayable> = emptyList<Displayable>().toMutableList()

    fun setPokemonList(pokemons: List<Displayable>) {
        items.clear()
        items.addAll(pokemons)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
        when (viewType) {
            ITEM_TYPE_POKEMON -> {
                val view =
                    LayoutInflater.from(parent.context).inflate(R.layout.main_item, parent, false)
                PokemonViewHolder(view)
            }
            ITEM_TYPE_HEADER -> {
                val view =
                    LayoutInflater.from(parent.context).inflate(R.layout.header_item, parent, false)
                HeaderViewHolder(view)
            }
            else -> {
                throw IllegalStateException()
            }
        }


    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val itemToShow = items[position]
        when (itemToShow) {
            is PokemonItem -> {
                (holder as PokemonViewHolder).bind(itemToShow)
            }
            is HeaderItem -> {
                (holder as HeaderViewHolder).bind(itemToShow)
            }

            else -> {
                throw IllegalStateException()
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (items[position]) {
            is PokemonItem -> ITEM_TYPE_POKEMON
            is HeaderItem -> ITEM_TYPE_HEADER
            else -> ITEM_TYPE_UNKNOWN
        }
    }

    class PokemonViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        private val textView = itemView.findViewById<TextView>(R.id.name)
        private val imageView = itemView.findViewById<ImageView>(R.id.imagePreview)
        private val card = itemView.findViewById<CardView>(R.id.pokemonCard)

        fun bind(item: PokemonItem) {
            textView.text = item.name.capitalize()
            Glide.with(imageView.context)
                .asBitmap()
                .load(item.image)
                .into(imageView)

            Glide.with(imageView.context)
                .asBitmap()
                .load(item.image)
                .into(object : BitmapImageViewTarget(imageView) {
                    override fun onResourceReady(
                        resource: Bitmap,
                        transition: Transition<in Bitmap>?
                    ) {
                        super.onResourceReady(resource, transition)
                        Palette.generateAsync(resource, object : Palette.PaletteAsyncListener {
                            override fun onGenerated(palette: Palette?) {
                                val swatch = if(palette != null) palette.vibrantSwatch else Palette.Swatch(1,1)
                                card.setCardBackgroundColor(darkenColor(swatch!!.rgb))
                            }
                        })
                    }
                })

//            bitmap?.let {
//                Palette.from(bitmap).generate(object : Palette.PaletteAsyncListener {
//                    override fun onGenerated(palette: Palette?) {
//                        val swatch: Palette.Swatch = palette!!.vibrantSwatch!!
//                        if (swatch != null) {
//                            card.setCardBackgroundColor(swatch.rgb)
//                        }
//                    }
//                })
        }

        fun darkenColor(color: Int): Int {
            return Color.HSVToColor(FloatArray(3).apply {
                Color.colorToHSV(color, this)
                this[2] *= 0.8f
            })
        }
    }

    class HeaderViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val header = itemView.findViewById<TextView>(R.id.headerText)
        fun bind(item: HeaderItem) {
            header.text = item.text
        }
    }

}





