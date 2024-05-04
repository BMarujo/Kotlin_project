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
import androidx.navigation.navArgument
import androidx.navigation.compose.rememberNavController

data class InvIngredient(@DrawableRes val image: Int, val title: String, val quantity: Int, val unit: String)
val Inventoryingredients = listOf(
    // image, title, quantity
    InvIngredient(R.drawable.flour, "Chicken", 1, "kg"),
    InvIngredient(R.drawable.juice, "Carrot", 200, "Unidades"),
    InvIngredient(R.drawable.flour, "Beef", 500, "g"),
    InvIngredient(R.drawable.flour, "Onion", 100, "Unidades"),
    InvIngredient(R.drawable.flour, "Potato", 300, "g"),
    InvIngredient(R.drawable.flour, "Pepper", 50, "g"),
    InvIngredient(R.drawable.flour, "Salt", 100, "g"),
    InvIngredient(R.drawable.flour, "Sugar", 200, "g"),
    InvIngredient(R.drawable.flour, "Flour", 500, "g"),
    InvIngredient(R.drawable.flour, "Milk", 1, "L"),
    InvIngredient(R.drawable.flour, "Egg", 12, "Unidades"),
    InvIngredient(R.drawable.flour, "Butter", 200, "g"),
    InvIngredient(R.drawable.flour, "Cheese", 500, "g"),
    InvIngredient(R.drawable.flour, "Bread", 1, "kg"),
    InvIngredient(R.drawable.flour, "Rice", 1, "kg"),
    InvIngredient(R.drawable.flour, "Pasta", 500, "g"),
    InvIngredient(R.drawable.flour, "Spaghetti", 500, "g"),
    InvIngredient(R.drawable.flour, "Apple", 1, "Unidades"),
    InvIngredient(R.drawable.flour, "Banana", 1, "Unidades"),
    InvIngredient(R.drawable.flour,"Orange", 1, "Unidades"),
)

@Composable
fun Navigation() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = Screen.Inventory.route){
        composable(route = Screen.Inventory.route){
            InventoryScreen(navController)
        }
        composable(
            route = Screen.ItemConfiguration.route + "/{itemimage}/{itemtitle}/{itemquantity}/{itemunit}",
            arguments = listOf(
                navArgument("itemimage") { type = NavType.ReferenceType },
                navArgument("itemtitle") { type = NavType.StringType },
                navArgument("itemquantity") { type = NavType.IntType },
                navArgument("itemunit") { type = NavType.StringType }
            )
        ) {entry ->
            ConfigurationPage(
                iconResource = entry.arguments?.getInt("itemimage") ?: R.drawable.flour,
                title = entry.arguments?.getString("itemtitle") ?: "Flour",
                quantity = entry.arguments?.getInt("itemquantity") ?: 500,
                unit = entry.arguments?.getString("itemunit") ?: "g",
                navController = navController
            )
        }
    }
}

@Composable
fun InventoryScreen(navController: NavController){
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Image(
            painter = painterResource(id = R.drawable.inventoryscreen_background),
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
                            text = "Clique no item para fazer alterações",
                            textAlign = TextAlign.Center,
                            fontWeight = FontWeight.Bold,
                            fontSize = MaterialTheme.typography.titleLarge.fontSize,
                        )
                    }
                }
                InventoryIngredients(inventoryingredients = Inventoryingredients, navController = navController)
            }
        }
    }
}

@Composable
fun InventoryIngredients(inventoryingredients: List<InvIngredient>, navController: NavController) {
    EasyGridInv(nColumns = 3, items = inventoryingredients) {
        IngredientCardInv(it.image, it.title, it.quantity, it.unit, Modifier,navController = navController)
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
    @DrawableRes iconResource: Int,
    title: String,
    quantity: Int,
    unit: String,
    modifier: Modifier,
    navController: NavController
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .padding(bottom = 16.dp)
            .clickable(
            onClick = {
                navController.navigate(Screen.ItemConfiguration.withArgs(
                    iconResource.toString(),
                    title,
                    quantity.toString(),
                    unit,
                ))
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
                painter = painterResource(id = iconResource),
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

var dialog = mutableStateOf(false)
@Composable
fun ConfigurationPage(@DrawableRes iconResource: Int,
                      title: String,
                      quantity: Int,
                      unit: String,
                      navController: NavController) {

    // image
    @DrawableRes val newiconResource = remember { mutableStateOf(iconResource) } // por agora n é editavel, visto q é preciso incluir camera
    val newtitle = remember { mutableStateOf(title) }
    val newquantity = remember { mutableIntStateOf(quantity) }
    val newunit = remember { mutableStateOf(unit) }



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
                        painter = painterResource(id = iconResource),
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
                        text = "${quantity} ${unit}",
                        fontSize = 14.sp,
                        color = Color.Black
                    )
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            TextField(
                value = "",
                onValueChange = { newtitle.value = it },
                label = { Text("Novo nome do item") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))
            TextField(
                value = "",
                onValueChange = { newquantity.value = it.toInt() },
                label = { Text("Nova quantidade do item") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))
            TextField(
                value = "",
                onValueChange = { newunit.value = it },
                label = { Text("Nova unidade do item") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = { dialog.value = true },
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
                // go back to inventory page
                onClick = { },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Green,
                    contentColor = Color.White
                )
            ) {
                Text("Guardar alterações")
            }
            // cancelar
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                // go back to inventory page
                onClick = { navController.popBackStack() },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Gray,
                    contentColor = Color.White
                )
            ) {
                Text("Cancelar")
            }
            if (dialog.value) {
                showdialog()
            }
        }
    }
}

@Composable
fun showdialog(){
    val openAlertDialog = remember { mutableStateOf(true) }
    // dialog to confirm item removal
    when {
        openAlertDialog.value -> {
            AlertDialog(
                onDismissRequest = { /* handle dialog close event */ },
                title = {
                    Text(text = "Remover Item")
                },
                text = {
                    Text("Tem a certeza que deseja remover este item?")
                },
                confirmButton = {
                    Button(
                        onClick = { /* handle item removal */ }
                    ) {
                        Text("Remover")
                    }
                },
                dismissButton = {
                    Button(
                        onClick = { // close dialog
                            openAlertDialog.value = false
                            dialog.value = false
                        }
                    ) {
                        Text("Cancelar")
                    }
                }
            )
        }
    }
}