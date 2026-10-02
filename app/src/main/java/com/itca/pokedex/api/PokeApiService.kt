package com.itca.pokedex.api

import com.itca.pokedex.model.*
import retrofit2.http.GET
import retrofit2.http.Path

interface PokeApiService {

    @GET("pokemon?limit=20")
    suspend fun getPokemon(): PokemonResponse

    @GET("pokemon/{id}")
    suspend fun getPokemonDetail(
        @Path("id") id: Int
    ): PokemonDetail
}