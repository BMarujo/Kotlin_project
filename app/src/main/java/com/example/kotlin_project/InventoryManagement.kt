package com.example.kotlin_project

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import coil.compose.rememberImagePainter
import com.example.kotlin_project.data.Ingredient


@Composable
fun InventoryManagement(viewModel: RecipeViewModel ) {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = Screen.Inventory.route) {
        composable(route = Screen.Inventory.route) {
            InventoryScreen(navController, viewModel)
        }
        composable(
            route = Screen.ItemConfiguration.route + "/{iconResource}/{title}/{quantity}/{unit}/{ingredient}",
            arguments = listOf(
                navArgument("iconResource") { type = NavType.StringType },
                navArgument("title") { type = NavType.StringType },
                navArgument("quantity") { type = NavType.StringType },
                navArgument("unit") { type = NavType.StringType }
            )
        ) { entry ->
            val iconResource = entry.arguments?.getString("iconResource") ?: ""
            val title = entry.arguments?.getString("title") ?: ""
            val quantity = entry.arguments?.getString("quantity")?.toDoubleOrNull() ?: 0.0
            val unit = entry.arguments?.getString("unit") ?: ""
            val ingredient_id = entry.arguments?.getString("ingredient")?.toIntOrNull() ?: 0

            ConfigurationPage(iconResource, title, quantity, unit, navController, viewModel, ingredient_id)
        }
    }
}
@Composable
fun InventoryScreen(navController: NavController, viewModel: RecipeViewModel) {
    val ingredients by viewModel.allIngredients.collectAsState()

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Image(
            painter = painterResource(id = R.drawable.inventoryscreen_background),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )
        LazyColumn(contentPadding = PaddingValues(top = 0.dp), state = rememberLazyListState()) {
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
                            text = "Click on an Ingredient to configure it",
                            textAlign = TextAlign.Center,
                            fontWeight = FontWeight.Bold,
                            fontSize = MaterialTheme.typography.titleLarge.fontSize,
                        )
                    }
                }
                InventoryIngredients(inventoryingredients = ingredients, navController = navController)
            }
        }
    }
}

@Composable
fun InventoryIngredients(inventoryingredients: List<Ingredient>, navController: NavController) {
    EasyGridInv(nColumns = 3, items = inventoryingredients) {
        IngredientCardInv(it.imageUrl, it.name, it.quantity, it.measurement, Modifier,navController = navController, it.id)
    }
}

@Composable
fun <T> EasyGridInv(nColumns: Int, items: List<T>, content: @Composable (T) -> Unit) {
    Column(Modifier.padding(16.dp)) {
        for (i in items.indices step nColumns) {
            Row {
                for (j in 0 until nColumns) {
                    if (i + j < items.size) {
                        Box(
                            contentAlignment = Alignment.TopCenter,
                            modifier = Modifier
                                .weight(1f)
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
fun IngredientCardInv(
    iconResource: String,
    title: String,
    quantity: Double,
    unit: String,
    modifier: Modifier,
    navController: NavController,
    ingredient_id: Int

) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .padding(bottom = 16.dp)
            .clickable(
                onClick = {
                    navController.navigate(
                        Screen.ItemConfiguration.withArgs(
                            iconResource,
                            title,
                            quantity.toString(),
                            unit,
                            ingredient_id.toString(),
                        )
                    )
                }
            )
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
            fontWeight = FontWeight.Medium,
            color = White
        )
        Text(
            text = "$quantity $unit",
            modifier = Modifier.width(100.dp),
            fontSize = 14.sp,
            color = White
        )
    }
}

@Composable
fun ConfigurationPage(
    iconResource: String,
    title: String,
    quantity: Double,
    unit: String,
    navController: NavController,
    viewModel: RecipeViewModel,
    ingredient_id: Int
) {
    val newTitle = remember { mutableStateOf(title) }
    val newQuantity = remember { mutableStateOf(quantity.toString()) }
    val newUnit = remember { mutableStateOf(unit) }
    val dialogState = remember { mutableStateOf(false) }

    if (dialogState.value) {
        showDialog(dialogState, viewModel, title, quantity, unit, ingredient_id, navController, iconResource)
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(16.dp)
    ) {
        item {
            Text(
                text = "Configurações do item",
                fontWeight = FontWeight.Bold,
                fontSize = 24.sp,
                modifier = Modifier.padding(bottom = 16.dp)
            )
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp).align(Alignment.CenterHorizontally)
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
                    Text(
                        text = title,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color.Black
                    )
                    Text(
                        text = "$quantity $unit",
                        fontSize = 14.sp,
                        color = Color.Black
                    )
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            TextField(
                value = newTitle.value,
                onValueChange = { newTitle.value = it },
                label = { Text("Novo nome do item") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))
            TextField(
                value = newQuantity.value,
                onValueChange = { newQuantity.value = it },
                label = { Text("Nova quantidade do item") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))
            TextField(
                value = newUnit.value,
                onValueChange = { newUnit.value = it },
                label = { Text("Nova unidade do item") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = { dialogState.value = true },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Red,
                    contentColor = Color.White
                )
            ) {
                Text("Remover item")
            }
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = {
                    viewModel.updateIngredient(
                        Ingredient(
                            imageUrl = iconResource,
                            name = newTitle.value,
                            quantity = newQuantity.value.toDouble(),
                            measurement = newUnit.value,
                            id = ingredient_id
                        )
                    )
                    navController.popBackStack()
                },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Green,
                    contentColor = Color.White
                )
            ) {
                Text("Guardar alterações")
            }
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = { navController.popBackStack() },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Gray,
                    contentColor = Color.White
                )
            ) {
                Text("Cancelar")
            }
        }
    }
}

@Composable
fun showDialog(dialogState: MutableState<Boolean>, viewModel: RecipeViewModel, title: String, quantity: Double, unit: String, ingredient_id: Int, navController: NavController, iconResource: String) {
    AlertDialog(
        onDismissRequest = { dialogState.value = false },
        title = { Text(text = "Remover Item") },
        text = { Text("Tem a certeza que deseja remover este item?") },
        confirmButton = {
            Button(
                onClick = {
                    // Handle item removal
                    viewModel.deleteIngredient (
                        Ingredient(
                            imageUrl = iconResource,
                            name = title,
                            quantity = quantity,
                            measurement = unit,
                            id = ingredient_id
                        )
                    )
                    dialogState.value = false
                    navController.popBackStack()
                }
            ) {
                Text("Remover")
            }
        },
        dismissButton = {
            Button(
                onClick = { dialogState.value = false }
            ) {
                Text("Cancelar")
            }
        }
    )
}
