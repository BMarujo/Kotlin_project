package com.example.kotlin_project

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
import androidx.compose.material3.Shapes
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.LightGray
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.accompanist.insets.ProvideWindowInsets
import com.google.accompanist.insets.statusBarsPadding

@Composable
fun MainFragment(recipe: Recipe, onCancel: () -> Unit) {
    val scrollState = rememberLazyListState()
    ProvideWindowInsets {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = White
        ) {
            Box {
                Content(recipe, scrollState, onCancel)
            }
        }
    }

}

@Composable
fun CircularButton(
    @DrawableRes iconResource: Int,
    color: Color = Gray,
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
fun Content(recipe: Recipe, scrollState: LazyListState, onCancel: () -> Unit){
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
                CircularButton(R.drawable.ic_arrow_back)
                CircularButton(R.drawable.ic_favorite)
            }
            Image(
                painter = painterResource(id = R.drawable.strawberry_pie_1),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )


            Text(
                recipe.category,
                fontWeight = FontWeight.Medium,
                modifier = Modifier
                    .clip(Shapes.small)
                    .background(LightGray)
                    .padding(vertical = 6.dp, horizontal = 16.dp)
            )
            Text(
                recipe.title,
                fontSize = 26.sp,
                fontWeight = FontWeight.Bold,
            )
            BasicInfo(recipe)
            Description(recipe)
            ServingCalculator()
            IngredientsHeader()
            IngredientsList(recipe)
            ShoppingListButton()
            //Reviews(recipe)
            //Images()
            Button(onClick = onCancel) {
                Text("Go back")
            }
        }
    }
}
/*
@Composable
fun Images() {
    Row(Modifier.padding(16.dp), horizontalArrangement = Arrangement.SpaceBetween) {
        Image(
            painter = painterResource(id = R.drawable.strawberry_pie_2),
            contentDescription = null,
            modifier = Modifier
                .weight(1f)
                .clip(Shapes.small)
        )
        Spacer(modifier = Modifier.weight(0.1f))
        Image(
            painter = painterResource(id = R.drawable.strawberry_pie_3),
            contentDescription = null,
            modifier = Modifier
                .weight(1f)
                .clip(Shapes.small)
        )
    }
}


@Composable
fun Reviews(recipe: Recipe) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column {
            Text(text = "Reviews", fontWeight = FontWeight.Bold)
            Text(recipe.reviews, color = DarkGray)
        }
        Button(
            onClick = { /*TODO*/ }, elevation = null, colors = ButtonDefaults.buttonColors(
                containerColor = Transparent, contentColor = Pink
            )
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("See all")
                Icon(
                    painter = painterResource(id = R.drawable.ic_arrow_right),
                    contentDescription = null
                )
            }
        }
    }
}
*/

@Composable
fun ShoppingListButton() {
    Button(
        onClick = { /*TODO*/ },
        elevation = null,
        shape = Shapes.small,
        colors = ButtonDefaults.buttonColors(
            containerColor = LightGray,
            contentColor = Color.Black
        ), modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Text(text = "Add to shopping list", modifier = Modifier.padding(8.dp))
    }

}

@Composable
fun IngredientsList(recipe: Recipe) {
    EasyGrid(nColumns = 3, items = recipe.ingredients) {
        IngredientCard(it.image, it.title, it.subtitle, Modifier)
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
                            contentAlignment = Alignment.TopCenter,
                            modifier = Modifier.weight(1f)
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
    @DrawableRes iconResource: Int,
    title: String,
    subtitle: String,
    modifier: Modifier
) {
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
            containerColor = Pink,
            contentColor = White
        ) else ButtonDefaults.buttonColors(
            containerColor = LightGray,
            contentColor = DarkGray
        )
    ) {
        Text(text)
    }
}

@Composable
fun ServingCalculator() {
    var value by remember { mutableStateOf(6) }
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .clip(Shapes.medium)
            .background(LightGray)
            .padding(horizontal = 16.dp)
    ) {

        Text(text = "Serving", Modifier.weight(1f), fontWeight = FontWeight.Medium)
        CircularButton(
            iconResource = R.drawable.ic_minus,
            elevation = null,
            color = Pink
        ) { value-- }
        Text(text = "$value", Modifier.padding(16.dp), fontWeight = FontWeight.Medium)
        CircularButton(
            iconResource = R.drawable.ic_plus,
            elevation = null,
            color = Pink
        ) { value++ }
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
        InfoColumn(R.drawable.ic_clock, recipe.cookingTime)
        InfoColumn(R.drawable.ic_flame, recipe.energy)
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

@Preview(showBackground = true)
@Composable
fun MainFragmentPreview() {
    MainFragment(strawberryCake, onCancel = {})
}


data class Recipe(
    val title: String,
    val category: String,
    val cookingTime: String,
    val energy: String,
    val rating: String,
    val description: String,
    val reviews: String,
    val ingredients: List<Ingredient>
)

data class Ingredient(@DrawableRes val image: Int, val title: String, val subtitle: String)

val strawberryCake = Recipe(
    title = "Strawberry Cake",
    category = "Desserts",
    cookingTime = "50 min",
    energy = "620 kcal",
    rating = "4,9",
    description = "This dessert is very tasty and not difficult to prepare. Also, you can replace strawberries with any other berry you like.",
    reviews = "84 photos     430 comments",
    ingredients = listOf(
        Ingredient(R.drawable.flour, "Flour", "450 g"),
        Ingredient(R.drawable.eggs, "Eggs", "4"),
        Ingredient(R.drawable.juice, "Lemon juice", "150 g"),
        Ingredient(R.drawable.strawberry, "Strawberry", "200 g"),
        Ingredient(R.drawable.suggar, "Sugar", "1 cup"),
        Ingredient(R.drawable.mind, "Mind", "20 g"),
        Ingredient(R.drawable.vanilla, "Vanilla", "1/2 teaspoon"),
    )
)

val Shapes = Shapes(
    small = RoundedCornerShape(12.dp),
    medium = RoundedCornerShape(20.dp),
    large = RoundedCornerShape(24.dp)
)

val Pink = Color(0xFFB50F53)
val Gray = Color(0xFFCBCBCB)
val DarkGray = Color(0xFFACACAC)
val White = Color(0xFFFFFFFF)