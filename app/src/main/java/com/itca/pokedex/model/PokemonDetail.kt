package com.itca.pokedex.model

import com.google.gson.annotations.SerializedName

data class PokemonDetail(
    val name: String,
    val weight: Int,
    val height: Int,
    val sprites: Sprites
)

data class Sprites(
    @SerializedName("front_default")
    val frontDefault: String?
)