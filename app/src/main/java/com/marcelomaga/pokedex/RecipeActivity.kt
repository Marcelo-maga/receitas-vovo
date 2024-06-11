package com.marcelomaga.pokedex

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.marcelomaga.pokedex.ui.theme.PokedexTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class RecipeActivity : ComponentActivity() {
    private val dao = InstanceRoom.database.recipeDAO()
    private val scope = MainScope()
    private var recipe by mutableStateOf<Recipe?>(null)

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Retrieve the intent and get the ID
        val id = intent?.getIntExtra("id", -1) ?: -1
        if (id != -1) {
            fetchRecipe(id)
        }

        val intent = Intent(this, MainActivity::class.java)
        val updateIntent = Intent(this, UpdateRecipeActivity::class.java)

        setContent {
            PokedexTheme {
                Scaffold(topBar = {
                    TopAppBar(
                        title = { recipe?.let { Text(text = it.name) } },
                        navigationIcon = {
                            IconButton(onClick = {
                                startActivity(intent)
                            }) {
                                Icon(
                                    imageVector = Icons.Default.ArrowBack,
                                    contentDescription = "Voltar"
                                )
                            }
                        },
                        actions = {
                            IconButton(onClick = {
                                updateIntent.putExtra("id", recipe?.id)
                                startActivity(updateIntent)
                            }) {
                                Icon(imageVector = Icons.Default.Edit, contentDescription = "Editar")
                            }
                            IconButton(onClick = {
                                deleteRecipe(recipe!!)
                                startActivity(intent)
                            }) {
                                Icon(imageVector = Icons.Default.Delete, contentDescription = "Apagar")
                            }
                        }
                    )
                }, content = { padding ->
                    Surface(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(padding),
                        color = MaterialTheme.colorScheme.background
                    ) {
                        RecipeInfo(recipe)
                    }
                })
            }
        }
    }

    private fun fetchRecipe(id: Int) {
        scope.launch {
            val fetchedRecipe = withContext(Dispatchers.IO) {
                dao.getRecipeById(id)
            }

            recipe = fetchedRecipe
        }
    }

    private fun deleteRecipe(recipe: Recipe) {
        scope.launch {
            withContext(Dispatchers.Default) {
                dao.delete(recipe)
            }
        }
    }

    @Composable
    fun RecipeInfo(recipe: Recipe?) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .padding(10.dp)

        ) {
            Column {
                Text(text = "Ingredientes:")
                Spacer(modifier = Modifier.height(10.dp))
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(100.dp)
                ) {
                    recipe?.let { Text(text = it.ingredients) }
                }
            }


            Spacer(modifier = Modifier.height(15.dp))

            Column {
                Text(text = "Instruções:")
                Spacer(modifier = Modifier.height(10.dp))
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight()

                ) {
                    recipe?.let { Text(text = it.instructions) }
                }
            }
        }
    }
}
