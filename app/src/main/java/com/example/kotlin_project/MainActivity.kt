package com.example.kotlin_project

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.kotlin_project.ui.theme.Kotlin_ProjectTheme
import androidx.compose.runtime.*
import androidx.compose.material3.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.ListItem
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.ui.graphics.Color
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Kotlin_ProjectTheme {
                var selectedItem by remember { mutableIntStateOf(0) }
                val items = listOf("Favorites", "Recipes", "Inventory", "Add")
                val icons = listOf(Icons.Filled.Favorite, Icons.Filled.AccountBox, Icons.Filled.List, Icons.Filled.Add)
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    HomePage()
                }
            }
        }
    }
}

@Composable
fun HomePage() {
    var selectedItem by remember { mutableIntStateOf(2) }
    val items = listOf("Favorites", "Recipes", "Home", "Inventory", "Add")
    val icons = listOf(Icons.Filled.Favorite, Icons.Filled.Edit, Icons.Filled.AccountBox, Icons.Filled.List, Icons.Filled.Add)
    Scaffold(
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
        }
    ) { padding ->
        // Conteúdo da tela atual
        val content = when (selectedItem) {
            0 -> FavoritesScreen()
            1 -> RecipesScreen()
            2 -> HomeScreen()
            3 -> InventoryScreen()
            4 -> AddScreen()
            else -> null
        }

        // Aplicando padding ao contêiner superior
        content?.let {
            Column(
                modifier = Modifier.padding(padding)
            ) {
                it
            }
        }
    }
}


@Composable
fun HomeScreen() {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Cumprimento ao usuário
        Text("Olá, Usuário X!")

        // Mensagem de exploração
        Text("Explore as várias funcionalidades")

        // Lista de itens clicáveis
        Spacer(modifier = Modifier.height(16.dp))
        Box(
            modifier = Modifier
                .padding(16.dp)
                .border(2.dp, Color.Gray, RoundedCornerShape(16.dp))
                .background(Color.White, RoundedCornerShape(16.dp))
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(2.dp)
            ) {
                // List Item
                ListItem(
                    modifier = Modifier.fillMaxWidth(),
                    headlineContent = { Text("Three line list item") },
                    supportingContent = {
                        Text("Secondary text that is long and perhaps goes onto another line")
                    },
                    leadingContent = {
                        Icon(
                            Icons.Filled.Favorite,
                            contentDescription = "Localized description",
                        )
                    },
                    trailingContent = { Text("meta") }
                )

                // Explore more button
                Button(
                    onClick = { /* Ação ao clicar no botão "Explore more" */ },
                    content = { Text("Explore more") },
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Gray)
                )
            }

        }


    }
}


@Composable
fun FavoritesScreen() {
    Text(text = "Favorites Screen")
}

@Composable
fun RecipesScreen() {
    Text(text = "Recipes Screen")
}

@Composable
fun InventoryScreen() {
    Text(text = "Inventory Screen")
}

@Composable
fun AddScreen() {
    Text(text = "Add Screen")
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    Kotlin_ProjectTheme {
        HomePage()
    }
}
