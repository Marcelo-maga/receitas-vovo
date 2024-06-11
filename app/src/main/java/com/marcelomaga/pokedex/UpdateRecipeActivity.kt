package com.marcelomaga.pokedex

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.marcelomaga.pokedex.ui.theme.PokedexTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class UpdateRecipeActivity : ComponentActivity() {
    private val dao = InstanceRoom.database.recipeDAO()
    private val scope = MainScope()
    private var recipe by mutableStateOf<Recipe?>(null)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val id = intent?.getIntExtra("id", -1) ?: -1
        if (id != -1) {
            fetchRecipe(id)
        }

        setContent {
            PokedexTheme {
                FormRecipe()
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

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun FormRecipe() {
        var name by remember { mutableStateOf("") }
        var ingredients by remember { mutableStateOf("") }
        var instructions by remember { mutableStateOf("") }

        recipe?.let {
            name = it.name
            ingredients = it.ingredients
            instructions = it.instructions
        }

        val intent = Intent(this, MainActivity::class.java)

        Scaffold(topBar = {
            TopAppBar(
                title = { Text(text = "Editar Receita") },
                navigationIcon = {
                    IconButton(onClick = {
                        startActivity(intent)
                    }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack, contentDescription = "Voltar"
                        )
                    }
                },
            )
        }, content = { padding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(
                        15.dp, padding.calculateTopPadding(), 15.dp, 15.dp
                    ), verticalArrangement = Arrangement.SpaceBetween

            ) {
                Column {
                    OutlinedTextField(
                        value = name,
                        onValueChange = { name = it },
                        label = { Text("Nome") },
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = ingredients,
                        onValueChange = { ingredients = it },
                        label = { Text("Ingredientes") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(120.dp)
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = instructions,
                        onValueChange = { instructions = it },
                        label = { Text("Instruções") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(450.dp)
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    shape = RoundedCornerShape(percent = 20), onClick = {
                        scope.launch {
                            withContext(Dispatchers.Default) {
                                if (name.isNotEmpty() && ingredients.isNotEmpty() && instructions.isNotEmpty()) {
                                    dao.update(
                                        Recipe(
                                            id = recipe!!.id,
                                            name = name,
                                            ingredients = ingredients,
                                            instructions = instructions
                                        )
                                    )

                                    startActivity(intent)
                                }
                            }
                        }
                    }, modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)

                ) {
                    Text("Editar Receita")
                }
            }
        })
    }
}