package com.example.kotlin_project

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import coil.compose.rememberImagePainter
import com.example.kotlin_project.data.Recipe
import com.google.accompanist.insets.statusBarsPadding
import com.google.firebase.firestore.FirebaseFirestore

// ViewModel to manage the recipes
class SocialNetworkViewModel : ViewModel() {

    private val _recipes = MutableLiveData<List<Recipe>>()
    val recipes: LiveData<List<Recipe>> get() = _recipes

    init {
        loadRecipes()
    }

    private fun loadRecipes() {
        val db = FirebaseFirestore.getInstance()
        val socialNetworkOfRecipes = db.collection("SocialNetworkOfRecipes")

        socialNetworkOfRecipes.get()
            .addOnSuccessListener { result ->
                val recipesList = mutableListOf<Recipe>()
                for (document in result) {
                    val recipe = Recipe(
                        ingredients = document.getString("ingredients").orEmpty(),
                        description = document.getString("description").orEmpty(),
                        name = document.getString("name").orEmpty(),
                        imageUrl = document.getString("image").orEmpty(),
                        calories = document.getLong("calories")?.toInt() ?: 0,
                        category = document.getString("category").orEmpty(),
                        timeToCook = document.getLong("timeToCook")?.toInt() ?: 0
                    )
                    recipesList.add(recipe)
                }
                _recipes.value = recipesList
            }
    }
}

@Composable
fun SocialNetworkScreen(navController: NavHostController, viewModel: SocialNetworkViewModel = viewModel()) {
    // Observe the LiveData
    val recipes = viewModel.recipes.observeAsState(emptyList()).value

    LazyColumn(modifier = Modifier.padding(8.dp)) {
        item {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .fillMaxWidth()
                    .statusBarsPadding()
                    .padding(horizontal = 16.dp)
            ) {
                CircularButton(iconResource = R.drawable.ic_arrow_back, onClick = { navController.popBackStack() })
            }
            Text(
                text = "Social Network",
                fontWeight = FontWeight.Bold,
                fontSize = 32.sp,
                modifier = Modifier.padding(8.dp)
            )
        }
        // Correct usage of items to display a list of recipes
        items(recipes) { recipe ->
            FirebaseRecipeListItem(recipe = recipe, navController = navController)
        }
    }
}

@Composable
fun FirebaseRecipeListItem(
    recipe: Recipe,
    navController: NavHostController
) {
    val sharedViewModel: SharedViewModel = hiltViewModel()
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        shape = RoundedCornerShape(4.dp),
        border = BorderStroke(1.dp, Color.LightGray),
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
                    navigateToMainFragment2(navController, recipe)
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
