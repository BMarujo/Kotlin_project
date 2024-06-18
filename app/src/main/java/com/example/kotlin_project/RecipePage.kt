package com.example.kotlin_project

import android.content.Context
import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ButtonElevation
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.LightGray
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import coil.compose.rememberImagePainter
import com.example.kotlin_project.data.Ingredient
import com.example.kotlin_project.data.Recipe
import com.google.accompanist.insets.ProvideWindowInsets
import com.google.accompanist.insets.statusBarsPadding
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json

@Composable
fun MainFragment(recipeId: Int, viewModel: RecipeViewModel = viewModel(), onCancel: () -> Unit) {
    val recipe by viewModel.recipe.collectAsState()
    val ingredients by viewModel.ingredients.collectAsState()

    viewModel.getRecipeById(recipeId)

    recipe?.let { recipe ->
        val ingredientNames = recipe.ingredients.split(", ")
        viewModel.getIngredientsByNames(ingredientNames)

        ProvideWindowInsets {
            Surface(
                modifier = Modifier.fillMaxSize(), color = White
            ) {
                Box {
                    Content(recipe, ingredients, rememberLazyListState(), onCancel, viewModel = viewModel)
                }
            }
        }
    }
}

@Composable
fun CircularButton(
    @DrawableRes iconResource: Int,
    color: Color = MaterialTheme.colorScheme.primary,
    elevation: ButtonElevation? = ButtonDefaults.buttonElevation(),
    onClick: () -> Unit = {}
) {
    Button(
        onClick = onClick,
        contentPadding = PaddingValues(),
        shape = Shapes.small,
        colors = ButtonDefaults.buttonColors(containerColor = White, contentColor = color),
        elevation = elevation,
        modifier = Modifier
            .width(38.dp)
            .height(38.dp)
    ) {
        Icon(painterResource(id = iconResource), null)
    }
}

@Composable
fun Content(
    recipe: Recipe, ingredients: List<Ingredient>, scrollState: LazyListState, onCancel: () -> Unit, viewModel: RecipeViewModel
) {
    LazyColumn(contentPadding = PaddingValues(top = 0.dp), state = scrollState) {
        item {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .fillMaxWidth()
                    .statusBarsPadding()
                    .padding(horizontal = 16.dp)
            ) {
                CircularButton(iconResource = R.drawable.ic_arrow_back, onClick = onCancel)
            }
            Image(
                painter = rememberImagePainter(recipe.imageUrl),
                contentDescription = null,
                Modifier
                    .height(200.dp)
                    .fillMaxWidth(),
                contentScale = ContentScale.Crop
            )

            Text(
                recipe.category,
                fontWeight = FontWeight.Medium,
                modifier = Modifier
                    .clip(Shapes.small)
                    .background(LightGray)
                    .padding(horizontal = 16.dp, vertical = 16.dp)
            )
            Text(
                recipe.name,
                fontSize = 26.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 16.dp)
            )
            BasicInfo(recipe)
            Description(recipe)
            IngredientsHeader()
            IngredientsList(ingredients)
            Read_Button(recipe.description, viewModel = viewModel)
        }
    }
}

@Composable
fun Read_Button(description: String, viewModel: RecipeViewModel) {
    Button(
        onClick = { viewModel.readDescription(description)},
        elevation = null,
        shape = Shapes.small,
        colors = ButtonDefaults.buttonColors(
            containerColor = LightGray, contentColor = Color.Black
        ),
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Text(text = "Read Instructions Out Loud", modifier = Modifier.padding(8.dp))
    }
}

//For Firebase
@Composable
fun MainFragment2(navController: NavHostController, recipeJson: String?, context: Context = LocalContext.current, onCancel: () -> Unit) {
    val backStackEntry = navController.previousBackStackEntry
    System.out.println("RecipeJSON:"+recipeJson)

    // Converte a string JSON de volta para um objeto Recipe
    val recipe = remember {
            Json.decodeFromString(Recipe.serializer(), recipeJson!!)
    }


    // Query the Firebase to get the ingredients data through its name
    val ingredientNames = recipe.ingredients.split(", ")
    val ingredientsState = remember { mutableListOf<IngredientData>() }

    LaunchedEffect(Unit) {
        val ingredients = fetchIngredients(ingredientNames)
        ingredientsState.addAll(ingredients)
    }


    ProvideWindowInsets {
        Surface(
            modifier = Modifier.fillMaxSize(), color = White
        ) {
            Box {
                recipe?.let {
                    Content2(it, ingredientsState , rememberLazyListState(), onCancel, navController)
                } ?: run {
                    // Exibe uma mensagem de erro ou lida com a situação de alguma forma
                    Text(text = "Erro: Receita não encontrada.")
                }
            }
        }
    }
}

@Composable
fun Content2(
    recipe: Recipe, ingredients: List<IngredientData>, scrollState: LazyListState, onCancel: () -> Unit, navController: NavHostController
) {
    LazyColumn(contentPadding = PaddingValues(top = 0.dp), state = scrollState) {
        item {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .fillMaxWidth()
                    .statusBarsPadding()
                    .padding(horizontal = 16.dp)
            ) {
                CircularButton(iconResource = R.drawable.ic_arrow_back, onClick = onCancel)
            }
            Image(
                painter = rememberImagePainter(recipe.imageUrl),
                contentDescription = null,
                Modifier
                    .height(200.dp)
                    .fillMaxWidth(),
                contentScale = ContentScale.Crop
            )

            Text(
                recipe.category,
                fontWeight = FontWeight.Medium,
                modifier = Modifier
                    .clip(Shapes.small)
                    .background(LightGray)
                    .padding(horizontal = 16.dp, vertical = 16.dp)
            )
            Text(
                recipe.name,
                fontSize = 26.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 16.dp)
            )
            BasicInfo(recipe)
            Description(recipe)
            IngredientsHeader()
            IngredientsList2(ingredients)
            Read_Button2(recipe.description, viewModel = viewModel())
        }
    }
}

@Composable
fun IngredientsList2(ingredients: List<IngredientData>) {
    EasyGrid(nColumns = 3, items = ingredients) { ingredient ->
        IngredientCard(ingredient.imageUrl,ingredient.name,ingredient.quantity.toString(),Modifier)
    }
}

@Composable
fun Read_Button2(description: String, viewModel: SocialNetworkViewModel) {
    Button(
        onClick = { viewModel.readDescription(description) },
        elevation = null,
        shape = Shapes.small,
        colors = ButtonDefaults.buttonColors(
            containerColor = LightGray, contentColor = Color.Black
        ),
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Text(text = "Read Instructions Out Loud", modifier = Modifier.padding(8.dp))
    }
}

@Composable
fun IngredientsList(ingredients: List<Ingredient>) {
    EasyGrid(nColumns = 3, items = ingredients) {
        IngredientCard(it.imageUrl, it.name, it.quantity.toString(), Modifier)
    }
}


@Composable
fun <T> EasyGrid(nColumns: Int, items: List<T>, content: @Composable (T) -> Unit) {
    Column(Modifier.padding(16.dp)) {
        for (i in items.indices step nColumns) {
            Row {
                for (j in 0 until nColumns) {
                    if (i + j < items.size) {
                        Box(
                            contentAlignment = Alignment.TopCenter, modifier = Modifier.weight(1f)
                        ) {
                            content(items[i + j])
                        }
                    } else {
                        Spacer(Modifier.weight(1f, fill = true))
                    }
                }
            }
        }
    }
}

@Composable
fun IngredientCard(
    iconResource: String, title: String, subtitle: String, modifier: Modifier
) {
    println("Ingredient name: $iconResource")
    println("Ingredient quantity: $title")
    println("Ingredient imageUrl: $subtitle:")
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier.padding(bottom = 16.dp)
    ) {
        Card(
            shape = Shapes.large,
            modifier = Modifier
                .width(100.dp)
                .height(100.dp)
                .padding(bottom = 8.dp)
        ) {
            Image(
                painter = rememberImagePainter(iconResource),
                contentDescription = null,
                modifier = Modifier
                    .padding(10.dp)
                    .height(100.dp)
                    .width(100.dp)
                    .clip(Shapes.extraLarge),
                contentScale = ContentScale.Crop
            )
        }
        Text(
            text = title,
            modifier = Modifier.width(100.dp),
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium
        )
        Text(text = subtitle, color = DarkGray, modifier = Modifier.width(100.dp), fontSize = 14.sp)
    }
}

@Composable
fun IngredientsHeader() {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .padding(horizontal = 16.dp, vertical = 16.dp)
            .clip(Shapes.medium)
            .background(LightGray)
            .fillMaxWidth()
            .height(44.dp)
    ) {
        TabButton("Ingredients", true, Modifier.weight(1f))
    }
}

@Composable
fun TabButton(text: String, active: Boolean, modifier: Modifier) {
    Button(
        onClick = { /*TODO*/ },
        shape = Shapes.medium,
        modifier = modifier.fillMaxHeight(),
        elevation = null,
        colors = if (active) ButtonDefaults.buttonColors(
            containerColor = Pink, contentColor = White
        ) else ButtonDefaults.buttonColors(
            containerColor = LightGray, contentColor = DarkGray
        )
    ) {
        Text(text)
    }
}


@Composable
fun Description(recipe: Recipe) {
    Text(
        text = recipe.description,
        fontWeight = FontWeight.Medium,
        modifier = Modifier.padding(horizontal = 16.dp, vertical = 16.dp)
    )
}

@Composable
fun BasicInfo(recipe: Recipe) {
    Row(
        horizontalArrangement = Arrangement.SpaceEvenly,
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 16.dp)
    ) {
        InfoColumn(R.drawable.ic_clock, recipe.timeToCook.toString())
        InfoColumn(R.drawable.ic_flame, recipe.calories.toString())
    }
}

@Composable
fun InfoColumn(@DrawableRes iconResource: Int, text: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Icon(
            painter = painterResource(id = iconResource),
            contentDescription = null,
            tint = Pink,
            modifier = Modifier.height(24.dp)
        )
        Text(text = text, fontWeight = FontWeight.Bold)
    }
}

suspend fun fetchIngredients(ingredientNames: List<String>): List<IngredientData> {
    val db = Firebase.firestore
    val ingredients = mutableListOf<IngredientData>()

    withContext(Dispatchers.IO) {
        ingredientNames.forEach { ingredientName ->
            try {
                val documents = db.collection("IngredientsData")
                    .whereEqualTo("name", ingredientName)
                    .get()
                    .await()

                for (document in documents) {
                    val ingredient = document.toObject(IngredientData::class.java)
                    ingredients.add(ingredient)
                    println("Ingredient added: $ingredient")
                }
            } catch (e: Exception) {
                println("Error getting documents: $e")
            }
        }
    }

    return ingredients
}


val Shapes = Shapes(
    small = RoundedCornerShape(12.dp),
    medium = RoundedCornerShape(20.dp),
    large = RoundedCornerShape(24.dp)
)

val Pink = Color(0xFFB50F53)
val Gray = Color(0xFFCBCBCB)
val DarkGray = Color(0xFFACACAC)
val White = Color(0xFFFFFFFF)
val Red = Color(0xFFD00036)