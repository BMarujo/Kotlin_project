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
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.LightGray
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun RecipesScreen() {
    LazyColumn(modifier = Modifier.padding(8.dp)) {
        item {
            Text(
                text = "Recipes",
                fontWeight = FontWeight.Bold,
                fontSize = 32.sp,
                modifier = Modifier.padding(8.dp)
            )
            SearchComponent()
            RecipeListItem()
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

        OutlinedTextField(value = text,
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
fun RecipeListItem() {

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
                painter = painterResource(id = R.drawable.strawberry_pie_1),
                contentDescription = null,
                Modifier
                    .height(200.dp)
                    .fillMaxWidth()
            )
            Spacer(modifier = Modifier.padding(4.dp))
            Text(
                text = "", fontSize = 24.sp, fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.padding(8.dp))
            Text(
                text = "Ingredients", fontSize = 20.sp, fontWeight = FontWeight.SemiBold
            )
            Text(
                text = "açucar\n" + "farinha\n" + "ovo"
            )
            Spacer(modifier = Modifier.padding(8.dp))


            Column {
                Text(
                    text = "Instructions", fontSize = 20.sp, fontWeight = FontWeight.SemiBold
                )
                Text(
                    text = "lista de instruções"
                )
            }
            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(
                    imageVector = Icons.Default.KeyboardArrowUp,
                    contentDescription = "Clear",
                    tint = Color.Black,
                    modifier = Modifier.align(
                        Alignment.CenterHorizontally
                    )

                )
            }

            Image(
                painter = painterResource(id = R.drawable.strawberry_pie_1),
                contentDescription = null,
                Modifier
                    .height(200.dp)
                    .fillMaxWidth()
            )
            Spacer(modifier = Modifier.padding(4.dp))
            Text(
                text = "", fontSize = 24.sp, fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.padding(8.dp))
            Text(
                text = "Ingredients", fontSize = 20.sp, fontWeight = FontWeight.SemiBold
            )
            Text(
                text = "açucar\n" + "farinha\n" + "ovo"
            )
            Spacer(modifier = Modifier.padding(8.dp))


            Column {
                Text(
                    text = "Instructions", fontSize = 20.sp, fontWeight = FontWeight.SemiBold
                )
                Text(
                    text = "lista de instruções"
                )
            }
            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(
                    imageVector = Icons.Default.KeyboardArrowUp,
                    contentDescription = "Clear",
                    tint = Color.Black,
                    modifier = Modifier.align(
                        Alignment.CenterHorizontally
                    )

                )
            }
            Image(
                painter = painterResource(id = R.drawable.strawberry_pie_1),
                contentDescription = null,
                Modifier
                    .height(200.dp)
                    .fillMaxWidth()
            )
            Spacer(modifier = Modifier.padding(4.dp))
            Text(
                text = "", fontSize = 24.sp, fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.padding(8.dp))
            Text(
                text = "Ingredients", fontSize = 20.sp, fontWeight = FontWeight.SemiBold
            )
            Text(
                text = "açucar\n" + "farinha\n" + "ovo"
            )
            Spacer(modifier = Modifier.padding(8.dp))


            Column {
                Text(
                    text = "Instructions", fontSize = 20.sp, fontWeight = FontWeight.SemiBold
                )
                Text(
                    text = "lista de instruções"
                )
            }
            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(
                    imageVector = Icons.Default.KeyboardArrowUp,
                    contentDescription = "Clear",
                    tint = Color.Black,
                    modifier = Modifier.align(
                        Alignment.CenterHorizontally
                    )

                )
            }
            Image(
                painter = painterResource(id = R.drawable.strawberry_pie_1),
                contentDescription = null,
                Modifier
                    .height(200.dp)
                    .fillMaxWidth()
            )
            Spacer(modifier = Modifier.padding(4.dp))
            Text(
                text = "", fontSize = 24.sp, fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.padding(8.dp))
            Text(
                text = "Ingredients", fontSize = 20.sp, fontWeight = FontWeight.SemiBold
            )
            Text(
                text = "açucar\n" + "farinha\n" + "ovo"
            )
            Spacer(modifier = Modifier.padding(8.dp))


            Column {
                Text(
                    text = "Instructions", fontSize = 20.sp, fontWeight = FontWeight.SemiBold
                )
                Text(
                    text = "lista de instruções"
                )
            }
            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(
                    imageVector = Icons.Default.KeyboardArrowUp,
                    contentDescription = "Clear",
                    tint = Color.Black,
                    modifier = Modifier.align(
                        Alignment.CenterHorizontally
                    )

                )
            }

        }
    }
}


@Preview
@Composable
fun PreviewRecipeListItem() {
    RecipesScreen()
}


