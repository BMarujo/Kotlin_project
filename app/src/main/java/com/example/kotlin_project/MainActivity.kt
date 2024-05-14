package com.example.kotlin_project

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.List
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.kotlin_project.ui.theme.Kotlin_ProjectTheme
import com.google.accompanist.insets.statusBarsPadding
import com.example.kotlin_project.data.AppContainer
import com.example.kotlin_project.data.AppDataContainer
import com.example.kotlin_project.data.RecipesRepository


class MainActivity : ComponentActivity() {
    private val appContainer: AppContainer by lazy {
        AppDataContainer(context = applicationContext)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Kotlin_ProjectTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    HomePage(appContainer.recipesRepository)
                }
            }
        }
    }
}


@Composable
fun HomePage( recipesRepository: RecipesRepository) {
    var selectedItem by remember { mutableIntStateOf(2) }
    val items = listOf("Favorites", "Recipes", "Home", "Inventory", "Add")
    val icons = listOf(Icons.Filled.Favorite, Icons.Filled.Edit, Icons.Filled.AccountBox, Icons.Filled.List, Icons.Filled.Add)
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
    Scaffold(

        topBar = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .statusBarsPadding()
                    .padding(horizontal = 16.dp)
            ) {
                CircularButton(R.drawable.baseline_home_24, onClick = { /* Handle Home button click */ })
                Text(text = "Go To Home", style = MaterialTheme.typography.bodySmall)
            }
        },
        bottomBar = {
            NavigationBar {
                items.forEachIndexed { index, item ->
                    NavigationBarItem(
                        icon = { Icon(icons[index], contentDescription = item) },
                        label = { Text(item) },
                        selected = selectedItem == index,
                        onClick = { selectedItem = index }
                    )
                }
            }
        },
        // para o temporizador ou para medir a temperatura
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    // Handle FAB click
                }
            ) {
                Icon(Icons.Filled.Add, contentDescription = "Localized description")
            }
        },
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        },
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    bottom = it.calculateBottomPadding() // para evitar que o conteúdo fique atrás da barra de navegação
                )
                .padding(
                    top = it.calculateTopPadding()
                )
            //.padding(it) // <<-- or simply this
        ) {
            // Conteúdo da tela atual
            val content = when (selectedItem) {
                0 -> FavoritesScreen()
                1 -> RecipesScreen( recipesRepository )
                2 -> HomeScreen()
                3 -> Navigation()
                4 -> Navigation2(scope, snackbarHostState, recipesRepository)
                else -> Text("Unknown screen")
            }

        }
    }
}


@Composable
fun HomeScreen() {
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Image(
            painter = painterResource(id = R.drawable.home_kotlinproject_icm),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )
        LazyColumn(contentPadding = PaddingValues(top = 0.dp), state = rememberLazyListState())
        {
            item {
                Box(
                    modifier = Modifier
                        .padding(16.dp)
                        .border(
                            2.dp, Brush.verticalGradient(
                                colors = listOf(
                                    Color.Transparent,
                                    Color.White
                                ),
                                startY = 500f,
                                endY = 0f
                            ), RoundedCornerShape(48, 48, 48, 16)
                        )
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(
                                    Color.Transparent,
                                    Color.White
                                ),
                                startY = 500f,
                                endY = 0f
                            ), RoundedCornerShape(48, 48, 48, 16)
                        )
                        .fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Text(
                            text = "Hi, User X!",
                            textAlign = TextAlign.Left,
                            fontWeight = FontWeight.Bold,
                            fontSize = MaterialTheme.typography.titleLarge.fontSize,
                        )
                        Text(
                            text = "Explore your inventory, favorite recipes and more",
                            textAlign = TextAlign.Left,
                            fontWeight = FontWeight.Bold,
                        )
                    }
                }

                // Lista de itens clicáveis
                Spacer(modifier = Modifier.height(16.dp))
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(6.dp),
                    verticalArrangement = Arrangement.spacedBy(2.dp),
                ) {
                    // List Item
                    Card(// blur background
                        modifier = Modifier
                            .padding(16.dp)
                            .border(
                                2.dp, Brush.horizontalGradient(
                                    colors = listOf(
                                        Color.Transparent,
                                        Color.White
                                    ),
                                    startX = 1500f,
                                    endX = 0f
                                ), RoundedCornerShape(16.dp)
                            )
                            .background(
                                Brush.horizontalGradient(
                                    colors = listOf(
                                        Color.Transparent,
                                        Color.White
                                    ),
                                    startX = 1500f,
                                    endX = 0f
                                ), RoundedCornerShape(16.dp)
                            ),
                            //.height(120.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = Color.Transparent,
                        ),
                    ) {
                        ListItem(
                            colors = ListItemDefaults.colors(
                                //transparent
                                containerColor = Color.Transparent,
                                headlineColor = Color.Black,
                                leadingIconColor = Color.Black,
                                overlineColor = Color.Black,
                                supportingColor = Color.Black,
                                trailingIconColor = Color.Black,
                            ),
                            headlineContent = {
                                Text(
                                    text = "Inventory",
                                    fontWeight = FontWeight.Bold,
                                )
                            },
                            supportingContent = {
                                Text(
                                    text = "Add, edit and remove items/ingredients from your inventory",
                                    textAlign = TextAlign.Left,
                                )
                            },
                            leadingContent = {
                                Icon(
                                    Icons.Filled.Favorite,
                                    contentDescription = "Localized description",
                                )
                            },
                            trailingContent = {
                                Button(
                                    onClick = { /* Ação ao clicar no botão "Explore more" */ },
                                    content = { Text("Explore more") },
                                    modifier = Modifier.align(Alignment.CenterHorizontally),
                                    colors = ButtonDefaults.buttonColors(containerColor = Color.White, contentColor = Color.Black)
                                )
                            }
                        )
                    }
                    Card(// blur background
                        modifier = Modifier
                            .padding(16.dp)
                            .border(
                                2.dp, Brush.horizontalGradient(
                                    colors = listOf(
                                        Color.Transparent,
                                        Color.White
                                    ),
                                    startX = 1500f,
                                    endX = 0f
                                ), RoundedCornerShape(16.dp)
                            )
                            .background(
                                Brush.horizontalGradient(
                                    colors = listOf(
                                        Color.Transparent,
                                        Color.White
                                    ),
                                    startX = 1500f,
                                    endX = 0f
                                ), RoundedCornerShape(16.dp)
                            ),
                            //.height(120.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = Color.Transparent,
                        ),
                    ) {
                        ListItem(
                            colors = ListItemDefaults.colors(
                                //transparent
                                containerColor = Color.Transparent,
                                headlineColor = Color.Black,
                                leadingIconColor = Color.Black,
                                overlineColor = Color.Black,
                                supportingColor = Color.Black,
                                trailingIconColor = Color.Black,
                            ),
                            headlineContent = {
                                Text(
                                    text = "Favorites",
                                    fontWeight = FontWeight.Bold,
                                )
                            },
                            supportingContent = {
                                Text(
                                    text = "See your favorite recipes and add new ones",
                                    textAlign = TextAlign.Left,
                                )
                            },
                            leadingContent = {
                                Icon(
                                    Icons.Filled.Favorite,
                                    contentDescription = "Localized description",
                                )
                            },
                            trailingContent = {
                                Button(
                                    onClick = { /* Ação ao clicar no botão "Explore more" */ },
                                    content = { Text("Explore more") },
                                    modifier = Modifier.align(Alignment.CenterHorizontally),
                                    colors = ButtonDefaults.buttonColors(containerColor = Color.White, contentColor = Color.Black)
                                )
                            }
                        )
                    }
                    Card(// blur background
                        modifier = Modifier
                            .padding(16.dp)
                            .border(
                                2.dp, Brush.horizontalGradient(
                                    colors = listOf(
                                        Color.Transparent,
                                        Color.White
                                    ),
                                    startX = 1500f,
                                    endX = 0f
                                ), RoundedCornerShape(16.dp)
                            )
                            .background(
                                Brush.horizontalGradient(
                                    colors = listOf(
                                        Color.Transparent,
                                        Color.White
                                    ),
                                    startX = 1500f,
                                    endX = 0f
                                ), RoundedCornerShape(16.dp)
                            ),
                            //.height(120.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = Color.Transparent,
                        ),
                    ) {
                        ListItem(
                            colors = ListItemDefaults.colors(
                                //transparent
                                containerColor = Color.Transparent,
                                headlineColor = Color.Black,
                                leadingIconColor = Color.Black,
                                overlineColor = Color.Black,
                                supportingColor = Color.Black,
                                trailingIconColor = Color.Black,
                            ),
                            headlineContent = {
                                Text(
                                    text = "Recipes",
                                    fontWeight = FontWeight.Bold,
                                )
                            },
                            supportingContent = {
                                Text(
                                    text = "Save your recipes and add new ones",
                                    textAlign = TextAlign.Left,
                                    modifier = Modifier.fillMaxSize()
                                    )
                            },
                            leadingContent = {
                                Icon(
                                    Icons.Filled.Favorite,
                                    contentDescription = "Localized description",
                                )
                            },
                            trailingContent = {
                                Button(
                                    onClick = { /* Ação ao clicar no botão "Explore more" */ },
                                    content = { Text("Explore more") },
                                    modifier = Modifier.align(Alignment.CenterHorizontally),
                                    colors = ButtonDefaults.buttonColors(containerColor = Color.White, contentColor = Color.Black)
                                )
                            }
                        )
                    }
                    Card(// blur background
                        modifier = Modifier
                            .padding(16.dp)
                            .border(
                                2.dp, Brush.horizontalGradient(
                                    colors = listOf(
                                        Color.Transparent,
                                        Color.White
                                    ),
                                    startX = 1500f,
                                    endX = 0f
                                ), RoundedCornerShape(16.dp)
                            )
                            .background(
                                Brush.horizontalGradient(
                                    colors = listOf(
                                        Color.Transparent,
                                        Color.White
                                    ),
                                    startX = 1500f,
                                    endX = 0f
                                ), RoundedCornerShape(16.dp)
                            ),
                            //.height(120.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = Color.Transparent,
                        ),
                    ) {
                        ListItem(
                            colors = ListItemDefaults.colors(
                                //transparent
                                containerColor = Color.Transparent,
                                headlineColor = Color.Black,
                                leadingIconColor = Color.Black,
                                overlineColor = Color.Black,
                                supportingColor = Color.Black,
                                trailingIconColor = Color.Black,
                            ),
                            headlineContent = {
                                Text(
                                    text = "Add Ingredients",
                                    fontWeight = FontWeight.Bold,
                                )
                            },
                            supportingContent = {
                                Text(
                                    text = "Add new ingredients to your inventory",
                                    textAlign = TextAlign.Left,
                                )
                            },
                            leadingContent = {
                                Icon(
                                    Icons.Filled.Favorite,
                                    contentDescription = "Localized description",
                                )
                            },
                            trailingContent = {
                                Button(
                                    onClick = { /* Ação ao clicar no botão "Explore more" */ },
                                    content = { Text("Explore more") },
                                    modifier = Modifier.align(Alignment.CenterHorizontally),
                                    colors = ButtonDefaults.buttonColors(containerColor = Color.White, contentColor = Color.Black)
                                )
                            }
                        )
                    }
                    
                    // to test the scroll
                    
                    Card(// blur background
                        modifier = Modifier
                            .padding(16.dp)
                            .border(
                                2.dp, Brush.horizontalGradient(
                                    colors = listOf(
                                        Color.Transparent,
                                        Color.White
                                    ),
                                    startX = 1500f,
                                    endX = 0f
                                ), RoundedCornerShape(16.dp)
                            )
                            .background(
                                Brush.horizontalGradient(
                                    colors = listOf(
                                        Color.Transparent,
                                        Color.White
                                    ),
                                    startX = 1500f,
                                    endX = 0f
                                ), RoundedCornerShape(16.dp)
                            ),
                            //.height(120.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = Color.Transparent,
                        ),
                    ) {
                        ListItem(
                            colors = ListItemDefaults.colors(
                                //transparent
                                containerColor = Color.Transparent,
                                headlineColor = Color.Black,
                                leadingIconColor = Color.Black,
                                overlineColor = Color.Black,
                                supportingColor = Color.Black,
                                trailingIconColor = Color.Black,
                            ),
                            headlineContent = {
                                Text(
                                    text = "Add Ingredients",
                                    fontWeight = FontWeight.Bold,
                                )
                            },
                            supportingContent = {
                                Text(
                                    text = "Add new ingredients to your inventory",
                                    textAlign = TextAlign.Left,
                                )
                            },
                            leadingContent = {
                                Icon(
                                    Icons.Filled.Favorite,
                                    contentDescription = "Localized description",
                                )
                            },
                            trailingContent = {
                                Button(
                                    onClick = { /* Ação ao clicar no botão "Explore more" */ },
                                    content = { Text("Explore more") },
                                    modifier = Modifier.align(Alignment.CenterHorizontally),
                                    colors = ButtonDefaults.buttonColors(containerColor = Color.White, contentColor = Color.Black)
                                )
                            }
                        )
                    }
                    Card(// blur background
                        modifier = Modifier
                            .padding(16.dp)
                            .border(
                                2.dp, Brush.horizontalGradient(
                                    colors = listOf(
                                        Color.Transparent,
                                        Color.White
                                    ),
                                    startX = 1500f,
                                    endX = 0f
                                ), RoundedCornerShape(16.dp)
                            )
                            .background(
                                Brush.horizontalGradient(
                                    colors = listOf(
                                        Color.Transparent,
                                        Color.White
                                    ),
                                    startX = 1500f,
                                    endX = 0f
                                ), RoundedCornerShape(16.dp)
                            ),
                            //.height(120.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = Color.Transparent,
                        ),
                    ) {
                        ListItem(
                            colors = ListItemDefaults.colors(
                                //transparent
                                containerColor = Color.Transparent,
                                headlineColor = Color.Black,
                                leadingIconColor = Color.Black,
                                overlineColor = Color.Black,
                                supportingColor = Color.Black,
                                trailingIconColor = Color.Black,
                            ),
                            headlineContent = {
                                Text(
                                    text = "Add Ingredients",
                                    fontWeight = FontWeight.Bold,
                                )
                            },
                            supportingContent = {
                                Text(
                                    text = "Add new ingredients to your inventory",
                                    textAlign = TextAlign.Left,
                                )
                            },
                            leadingContent = {
                                Icon(
                                    Icons.Filled.Favorite,
                                    contentDescription = "Localized description",
                                )
                            },
                            trailingContent = {
                                Button(
                                    onClick = { /* Ação ao clicar no botão "Explore more" */ },
                                    content = { Text("Explore more") },
                                    modifier = Modifier.align(Alignment.CenterHorizontally),
                                    colors = ButtonDefaults.buttonColors(containerColor = Color.White, contentColor = Color.Black)
                                )
                            }
                        )
                    }
                }
            }
        }
    }
}


@Composable
fun FavoritesScreen() {
    Text(text = "Favorites Screen")
}


/*
@Preview(showBackground = true)
@Composable
fun FavoritesPreview() {
    Kotlin_ProjectTheme {
        FavoritesScreen()
    }
}

@Preview(showBackground = true)
@Composable
fun RecipesPreview() {
    Kotlin_ProjectTheme {
        RecipesScreen()
    }
}
*/

/*
@Preview(showBackground = true)
@Composable
fun AddPreview() {
    Kotlin_ProjectTheme {
        AddScreen()
    }
}*/