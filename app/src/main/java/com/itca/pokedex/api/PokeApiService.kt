package com.itca.pokedex.api

import com.itca.pokedex.model.PokemonDetail
import com.itca.pokedex.model.PokemonResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface PokeApiService {
    @GET("pokemon")
    suspend fun getPokemon(
        @Query("limit") limit: Int = 20
    ): PokemonResponse

    // Nuevo método para traer los detalles de un Pokémon por su nombre
    @GET("pokemon/{name}")
    suspend fun getPokemonDetail(
        @Path("name") name: String
    ): PokemonDetail
}