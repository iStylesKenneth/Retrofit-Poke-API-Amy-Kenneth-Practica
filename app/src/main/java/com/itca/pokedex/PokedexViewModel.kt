package com.itca.pokedex

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.itca.pokedex.api.RetrofitClient
import com.itca.pokedex.model.PokemonDetail
import com.itca.pokedex.model.PokemonItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed class UiState<out T> {
    object Loading : UiState<Nothing>()
    data class Success<T>(val data: T) : UiState<T>()
    data class Error(val message: String) : UiState<Nothing>()
}

class PokedexViewModel : ViewModel() {

    private val _listState = MutableStateFlow<UiState<List<PokemonItem>>>(UiState.Loading)
    val listState: StateFlow<UiState<List<PokemonItem>>> = _listState.asStateFlow()

    private val _detailState = MutableStateFlow<UiState<PokemonDetail>?>(null)
    val detailState: StateFlow<UiState<PokemonDetail>?> = _detailState.asStateFlow()

    init {
        fetchPokemonList()
    }

    fun fetchPokemonList() {
        viewModelScope.launch {
            _listState.value = UiState.Loading
            try {
                val response = RetrofitClient.apiService.getPokemon(limit = 100)
                _listState.value = UiState.Success(response.results)
            } catch (e: Exception) {
                _listState.value = UiState.Error(e.localizedMessage ?: "Error al conectar con el servidor")
            }
        }
    }

    fun fetchPokemonDetail(name: String) {
        viewModelScope.launch {
            _detailState.value = UiState.Loading
            try {
                val detail = RetrofitClient.apiService.getPokemonDetail(name)
                _detailState.value = UiState.Success(detail)
            } catch (e: Exception) {
                _detailState.value = UiState.Error(e.localizedMessage ?: "Error al cargar los detalles")
            }
        }
    }

    fun clearDetail() {
        _detailState.value = null
    }
}