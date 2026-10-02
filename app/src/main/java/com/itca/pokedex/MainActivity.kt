package com.itca.pokedex

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.itca.pokedex.api.RetrofitClient
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
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    PokemonScreen(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

@Composable
fun PokemonScreen(modifier: Modifier = Modifier) {
    var pokemonList by remember { mutableStateOf(emptyList<PokemonItem>()) }
    var isLoading by remember { mutableStateOf(true) }

    // Cargar los datos al iniciar la pantalla
    LaunchedEffect(Unit) {
        try {
            val response = withContext(Dispatchers.IO) {
                RetrofitClient.apiService.getPokemon()
            }
            pokemonList = response.results
        } catch (_: Exception) {
            // Manejo de errores silencioso para la prueba
        } finally {
            isLoading = false
        }
    }

    Box(modifier = modifier.fillMaxSize()) {
        if (isLoading) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
        } else {
            LazyColumn(modifier = Modifier.fillMaxSize().padding(16.dp)) {
                items(pokemonList) { pokemon ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp)
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