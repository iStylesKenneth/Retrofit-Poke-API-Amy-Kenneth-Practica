package com.itca.pokedex.model

data class PokemonResponse(
    val results: List<PokemonItem>
)

data class PokemonItem(
    val name: String,
    val url: String
)