package com.itca.pokedex.model

data class PokemonResponse(
    val count: Int,
    val next: String?,
    val previous: String?,
    val results: List<PokemonItem>
)

data class PokemonItem(
    val name: String,
    val url: String
)

data class PokemonDetail(
    val id: Int,
    val name: String,
    val height: Int,
    val weight: Int,
    val sprites: PokemonSprites
)

data class PokemonSprites(
    val frontDefault: String?
)