package com.itca.pokedex

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.itca.pokedex.api.RetrofitClient
import com.itca.pokedex.model.PokemonDetail
import com.itca.pokedex.model.PokemonItem
import com.itca.pokedex.ui.theme.PokedexTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PokedexTheme {
                PokedexApp()
            }
        }
    }
}

@Composable
fun PokedexApp() {
    var selectedPokemonName by remember { mutableStateOf<String?>(null) }

    if (selectedPokemonName == null) {
        PokemonListScreen(
            onPokemonClick = { pokemonName ->
                selectedPokemonName = pokemonName
            }
        )
    } else {
        PokemonDetailScreen(
            pokemonName = selectedPokemonName!!,
            onBackClick = {
                selectedPokemonName = null
            }
        )
    }
}

@Composable
fun PokemonListScreen(onPokemonClick: (String) -> Unit) {
    var pokemonList by remember { mutableStateOf(emptyList<PokemonItem>()) }
    var isLoading by remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        try {
            val response = withContext(Dispatchers.IO) {
                RetrofitClient.apiService.getPokemon()
            }
            pokemonList = response.results
        } catch (_: Exception) {
        } finally {
            isLoading = false
        }
    }

    Scaffold(
        topBar = {
            @OptIn(ExperimentalMaterial3Api::class)
            TopAppBar(title = { Text("Pokédex - Lista") })
        }
    ) { innerPadding ->
        Box(modifier = Modifier.fillMaxSize().padding(innerPadding)) {
            if (isLoading) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            } else {
                LazyColumn(modifier = Modifier.fillMaxSize().padding(16.dp)) {
                    items(pokemonList) { pokemon ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp),
                            onClick = { onPokemonClick(pokemon.name) }
                        ) {
                            Text(
                                text = pokemon.name.replaceFirstChar { it.uppercase() },
                                modifier = Modifier.padding(16.dp),
                                style = MaterialTheme.typography.titleMedium,
                                fontSize = 18.sp
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun PokemonDetailScreen(pokemonName: String, onBackClick: () -> Unit) {
    var pokemonDetail by remember { mutableStateOf<PokemonDetail?>(null) }
    var isLoading by remember { mutableStateOf(true) }

    LaunchedEffect(pokemonName) {
        try {
            val detail = withContext(Dispatchers.IO) {
                RetrofitClient.apiService.getPokemonDetail(pokemonName)
            }
            pokemonDetail = detail
        } catch (_: Exception) {
        } finally {
            isLoading = false
        }
    }

    Scaffold(
        topBar = {
            @OptIn(ExperimentalMaterial3Api::class)
            TopAppBar(
                title = { Text("Detalle de Pokémon") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Regresar")
                    }
                }
            )
        }
    ) { innerPadding ->
        Box(modifier = Modifier.fillMaxSize().padding(innerPadding)) {
            if (isLoading) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            } else if (pokemonDetail != null) {
                val detail = pokemonDetail!!
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    AsyncImage(
                        model = detail.sprites.frontDefault,
                        contentDescription = detail.name,
                        modifier = Modifier.size(180.dp)
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = detail.name.replaceFirstChar { it.uppercase() },
                        style = MaterialTheme.typography.headlineLarge,
                        fontSize = 32.sp
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "Altura: ${detail.height} | Peso: ${detail.weight}",
                        style = MaterialTheme.typography.bodyLarge,
                        fontSize = 18.sp
                    )
                }
            }
        }
    }
}