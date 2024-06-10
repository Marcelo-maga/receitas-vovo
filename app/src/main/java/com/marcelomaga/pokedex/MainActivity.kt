package com.marcelomaga.pokedex

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.marcelomaga.pokedex.ui.theme.PokedexTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


@OptIn(ExperimentalMaterial3Api::class)
class MainActivity : ComponentActivity() {

    private val scope = MainScope()
    private val recipes = mutableStateListOf<Recipe>()
    private val dao = InstanceRoom.database.recipeDAO()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()


        setContent {
            PokedexTheme {
                Scaffold(floatingActionButton = {
                    FloatingActionButton(onClick = {
                        val intent = Intent(this, NewRecipeActivity::class.java)
                        startActivity(intent)
                    }) {
                        Icon(imageVector = Icons.Default.Add, contentDescription = "Adicionar")
                    }
                }, topBar = {
                    TopAppBar(
                        title = { Text(text = "Receitas Da Vovó") },
                    )
                }, content = { padding ->
                    Surface(
                        modifier = Modifier.fillMaxSize(),
                        color = MaterialTheme.colorScheme.background
                    ) {
                        ListaLugares(
                            recipes,
                            onEditClick = {
                                println("cuzinh2")
                            }
                        )

//                        FormRecipe()

                        getRecipes()
                    }
                })


            }
        }
    }

    override fun onResume() {
        super.onResume()
        recipes.clear()
        getRecipes()
    }

    private fun getRecipes() {
        scope.launch {
            withContext(Dispatchers.Default) {
                dao.getAllRecipes().forEach { recipe ->
                    recipes.add(recipe)
                }
            }
        }
    }
}

@Composable
fun ListaLugares(
    recipes: SnapshotStateList<Recipe>, onEditClick: () -> Unit
) {
    var searchText by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            TextField(
                value = searchText,
                onValueChange = { searchText = it },
                modifier = Modifier.weight(1f),
                placeholder = { Text("Buscar receitas...") },
                textStyle = TextStyle(color = Color.White),
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn(modifier = Modifier
            .fillMaxWidth()
            .padding(5.dp)) {
            items(recipes) { recipe ->
                key(recipe.id) {
                    Card(
                        modifier = Modifier
                            .padding(8.dp)
                            .height(100.dp)
                            .fillMaxWidth()
                    ) {
                        Row(modifier = Modifier
                            .padding(10.dp)
                            .fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Column {
                                Text(text = "${recipe.name}", fontWeight = FontWeight.Bold, fontSize = 25.sp)
                            }
//                            Column {
//                                IconButton(onClick = { onEditClick() }) {
//                                    Icon(
//                                        imageVector = Icons.Default.Edit,
//                                        contentDescription = ""
//                                    )
//                                }
//                                IconButton(onClick = {}) {
//                                    Icon(
//                                        imageVector = Icons.Default.Delete,
//                                        contentDescription = ""
//                                    )
//                                }
//                            }

                        }
                    }
                }
            }
        }
    }
}
