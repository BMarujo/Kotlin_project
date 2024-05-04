package com.example.kotlin_project

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

val ingredients = listOf("Rice", "Onion", "Carrots")

@Composable
fun AddRecipePage() {
    val nameState = remember { mutableStateOf("") }
    val categoryState = remember { mutableStateOf("") }
    val descriptionState = remember { mutableStateOf("") }
    val ingredientsList = remember { mutableStateListOf<String>() }
    val timeToCookState = remember { mutableStateOf("") }
    val caloriesState = remember { mutableStateOf("") }
    var expanded by remember { mutableStateOf(false) }

    var selectedIngredient by remember { mutableStateOf("") }

    LazyColumn(modifier = Modifier.padding(16.dp)) {
        item {
            OutlinedTextField(value = nameState.value,
                onValueChange = { nameState.value = it },
                label = { Text("Name") },
                modifier = Modifier.fillMaxWidth()
            )
        }

        item {
            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(value = categoryState.value,
                onValueChange = { categoryState.value = it },
                label = { Text("Category") },
                modifier = Modifier.fillMaxWidth()
            )
        }

        item {
            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(value = descriptionState.value,
                onValueChange = { descriptionState.value = it },
                label = { Text("Description") },
                modifier = Modifier.fillMaxWidth()
            )
        }

        item {
            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(value = timeToCookState.value,
                onValueChange = { timeToCookState.value = it },
                label = { Text("Time to Cook") },
                modifier = Modifier.fillMaxWidth()
            )
        }

        item {
            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(value = caloriesState.value,
                onValueChange = { caloriesState.value = it },
                label = { Text("Calories") },
                modifier = Modifier.fillMaxWidth()
            )
        }

        item {
            Spacer(modifier = Modifier.height(16.dp))

            // Button to add new ingredient
            Button(onClick = {
                expanded = true
            }) {
                Text("Add Ingredient")
            }
        }
        if (expanded) {
            item {
                DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                    ingredients.forEach { ingredient ->
                        DropdownMenuItem(onClick = {
                            ingredientsList.add(ingredient)
                            selectedIngredient = ingredient
                            expanded = false
                        }, text = { Text(ingredient) })
                    }
                }
            }
        }
        item {
            Spacer(modifier = Modifier.height(16.dp))

            // Ingredient List
            ingredientsList.forEachIndexed { index, ingredient ->
                OutlinedTextField(value = ingredient,
                    onValueChange = { ingredientsList[index] = it },
                    label = { Text("Ingredient ${index + 1}") },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }



        item {
            Spacer(modifier = Modifier.height(16.dp))

            // Button to finish adding recipe
            Button(onClick = {
                // Perform action to save recipe
            }) {
                Text("Finish")
            }
        }
    }
}


@Preview
@Composable
fun AddRecipePreview() {
    AddRecipePage()
}