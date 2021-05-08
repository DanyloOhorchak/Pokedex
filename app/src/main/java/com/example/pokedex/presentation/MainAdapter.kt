package com.example.pokedex.presentation

import android.content.res.ColorStateList
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.pokedex.R
import com.example.pokedex.domain.PokemonEntity
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import java.lang.IllegalStateException

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


    class MainViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val textView = itemView.findViewById<TextView>(R.id.name)
        private val imagePreview = itemView.findViewById<ImageView>(R.id.imagePreview)

        fun bind(item: PokemonEntity) {
            textView.text = item.name
            Picasso.get().load(item.image).into(imagePreview, object : Callback {
                override fun onSuccess() {
                    Log.d("", "Loaded image")
                }

                override fun onError(e: Exception?) {
                    Log.d("", "Loaded image", e)
                }
            })
        }
    }

    class PokemonViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        private val textView = itemView.findViewById<TextView>(R.id.name)
        private val imagePreview = itemView.findViewById<ImageView>(R.id.imagePreview)

        fun bind(item: PokemonItem) {
            textView.text = item.name
            if (item.useColor) {
                textView.setTextColor(Color.GREEN)
            } else {
                textView.setTextColor(Color.BLACK)
            }
            Picasso.get().load(item.image).into(imagePreview, object : Callback {
                override fun onSuccess() {
                    Log.d("", "Loaded image")
                }

                override fun onError(e: Exception?) {
                    Log.d("", "Loaded image", e)
                }
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

