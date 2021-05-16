package com.example.pokedex.presentation

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.pokedex.data.NetworkPokemonRepository
import com.example.pokedex.data.createPokemonService
import com.example.pokedex.di.Injector
import com.example.pokedex.domain.RepositoryCallback
import com.example.pokedex.domain.PokemonEntity
import com.example.pokedex.domain.PokemonRepository
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.schedulers.Schedulers

class MainViewModel : ViewModel() {
    private val repository: PokemonRepository = Injector.getPokemonRepository()
    private var disposable: Disposable? = null

    private val viewStateLiveData = MutableLiveData<MainViewState>()
    fun viewState(): LiveData<MainViewState> = viewStateLiveData

    fun loadData() {
        viewStateLiveData.value = MainViewState.LoadingState

        disposable = repository.getPokemonList().map { it.map { it.toItem() } }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { viewStateLiveData.value = MainViewState.ContentState(it) },
                {
                    Log.d("ViewModel", "Error is", it)
                    viewStateLiveData.value = MainViewState.ErrorState("Unable to load data")
                })
    }

    override fun onCleared() {
        super.onCleared()
        disposable?.dispose()
    }
}
