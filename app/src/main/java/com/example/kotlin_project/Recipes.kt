package com.example.kotlin_project

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.LightGray
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import coil.compose.rememberImagePainter
import com.example.kotlin_project.data.Recipe
import com.example.kotlin_project.data.RecipesRepository


enum class RecipesScreenEnum() {
    Recipes,
    RecipesDetails,
}

@Composable
fun RecipesScreen(
    recipesRepository: RecipesRepository,
    navController: NavHostController = rememberNavController()
) {
    NavHost(
        navController = navController,
        startDestination = RecipesScreenEnum.Recipes.name,
    ) {
        composable(route = RecipesScreenEnum.Recipes.name) {
            MainRecipesScreen(
                viewModel = RecipeViewModel(recipesRepository),
                navController = navController

            )
        }
        composable(
            route = "${RecipesScreenEnum.RecipesDetails.name}/{recipeId}",
            arguments = listOf(navArgument("recipeId") { type = NavType.IntType })
        ) { navBackStackEntry ->
            val recipeId = navBackStackEntry.arguments?.getInt("recipeId")
            val viewModel = RecipeViewModel(recipesRepository)

                recipeId?.let { id ->
                    MainFragment(
                        recipeId = id,
                        viewModel = viewModel,
                        onCancel = {
                            navController.popBackStack(RecipesScreenEnum.Recipes.name, inclusive = false)
                        },
                    )
                }
        }

    }
}

@Composable
fun MainRecipesScreen(
    viewModel: RecipeViewModel,
    navController: NavHostController
) {
    val allRecipes by viewModel.allRecipes.collectAsState(initial = emptyList())

    LazyColumn(modifier = Modifier.padding(8.dp)) {
        item {
            Text(
                text = "Recipes",
                fontWeight = FontWeight.Bold,
                fontSize = 32.sp,
                modifier = Modifier.padding(8.dp)
            )
            SearchComponent()
            allRecipes.forEach { recipe ->
                RecipeListItem(
                    recipe = recipe,
                    navController = navController
                )
            }
        }
    }
}
@Composable
fun SearchComponent() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp)
    ) {

        var text by remember { mutableStateOf("") }

        OutlinedTextField(
            value = text,
            onValueChange = { text = it },
            label = { Text("Search") },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Search",
                    tint = Color.Black
                )
            },
            modifier = Modifier.fillMaxWidth()
        )


    }
}
@Composable
fun RecipeListItem(
    recipe: Recipe,
    navController: NavHostController
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        shape = RoundedCornerShape(4.dp),
        border = BorderStroke(1.dp, LightGray),
        colors = CardDefaults.cardColors(
            Color.White
        )
    ) {
        Column(
            modifier = Modifier.padding(8.dp)
        ) {
            Image(
                painter = rememberImagePainter(recipe.imageUrl),
                contentDescription = null,
                Modifier
                    .height(200.dp)
                    .fillMaxWidth(),
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.padding(4.dp))
            Text(
                text = recipe.name,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.padding(8.dp))
            Text(
                text = "Ingredients",
                fontSize = 20.sp,
                fontWeight = FontWeight.SemiBold
            )
            Text(text = recipe.ingredients)

            Spacer(modifier = Modifier.padding(8.dp))
            Column {
                Text(
                    text = "Instructions",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    text = recipe.description
                )
            }
            Spacer(modifier = Modifier.padding(8.dp))

            Spacer(modifier = Modifier.padding(8.dp))
            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                Button(onClick = {
                    navController.navigate("${RecipesScreenEnum.RecipesDetails.name}/${recipe.id}") {
                        popUpTo(RecipesScreenEnum.Recipes.name) { inclusive = false }
                        }
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Pink, contentColor = White)
                ) {
                    Text(text = "Open Recipe's Details")

                }

            }
        }
    }
}

