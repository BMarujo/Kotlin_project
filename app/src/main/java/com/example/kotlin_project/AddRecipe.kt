package com.example.kotlin_project

import android.content.Context
import android.net.Uri
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

val ingredients = listOf("Rice", "Onion", "Carrots")

@Composable
fun AddRecipePage(navController: NavController, scope: CoroutineScope, snackbarHostState: SnackbarHostState, context: Context = LocalContext.current) {
    val nameState = remember { mutableStateOf("") }
    val categoryState = remember { mutableStateOf("") }
    val descriptionState = remember { mutableStateOf("") }
    val ingredientsList = remember { mutableStateListOf<String>() }
    val timeToCookState = remember { mutableStateOf("") }
    val caloriesState = remember { mutableStateOf("") }
    var expanded by remember { mutableStateOf(false) }

    var selectedIngredient by remember { mutableStateOf("") }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(16.dp),
        contentPadding = PaddingValues(top = 0.dp), state = rememberLazyListState(),
    ) {
        item{
            Button(onClick = { navController.navigate(Screen.ChooseItemOrRecipe.route) }) {
                Text("Go Back")
            }
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Details of the New Recipe",
                textAlign = androidx.compose.ui.text.style.TextAlign.Center,
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
                    modifier = Modifier
                        .padding(16.dp)
                        .align(Alignment.CenterHorizontally)
                ) {
                    val result = remember { mutableStateOf<Uri?>(null) }
                    val galleryLauncher = rememberLauncherForActivityResult(ActivityResultContracts.PickVisualMedia()) {
                        result.value = it
                    }
                    val cameraLauncher = rememberLauncherForActivityResult(ActivityResultContracts.TakePicturePreview()) { bitmap ->
                        // Exibir o preview da imagem
                        result.value = bitmap?.toUri(context)
                    }

                    result.value?.let { image ->
                        val painter = rememberAsyncImagePainter(
                            ImageRequest
                                .Builder(LocalContext.current)
                                .data(data = image)
                                .build()
                        )
                        Image(
                            painter = painter,
                            contentDescription = null,
                            modifier = Modifier
                                .padding(10.dp)
                                .height(100.dp)
                                .width(100.dp)
                                .clip(Shapes.extraLarge)
                                .align(Alignment.CenterHorizontally),
                            contentScale = ContentScale.Crop
                        )
                    }
                    // IDK if its working
                    /*
                    result.value?.let { image ->
                        val savedUri = saveImageToLocal(context, image)
                        // Agora você tem a URI da imagem salva localmente que pode ser usada conforme necessário
                    }
                     */
                    Button(onClick = {
                        galleryLauncher.launch(
                            PickVisualMediaRequest(mediaType = ActivityResultContracts.PickVisualMedia.ImageOnly)
                        )},modifier = Modifier.fillMaxSize()) {
                        Text(text = "Select Image")
                    }

                    Spacer(modifier = Modifier.width(16.dp))

                    Button(
                        onClick = {
                            requestCameraPermission(context as ComponentActivity) {
                                // A permissão foi concedida, lançar a câmera
                                cameraLauncher.launch(null)
                            }
                        },
                        modifier = Modifier.fillMaxSize()
                    ) {
                        Text(text = "Take Photo")
                    }

                }
            }
        }
        item {
            TextField(value = nameState.value,
                onValueChange = { nameState.value = it },
                label = { Text("Name") },
                modifier = Modifier.fillMaxWidth()
            )
        }

        item {
            Spacer(modifier = Modifier.height(16.dp))

            TextField(value = categoryState.value,
                onValueChange = { categoryState.value = it },
                label = { Text("Category") },
                modifier = Modifier.fillMaxWidth()
            )
        }

        item {
            Spacer(modifier = Modifier.height(16.dp))

            TextField(value = descriptionState.value,
                onValueChange = { descriptionState.value = it },
                label = { Text("Description") },
                modifier = Modifier.fillMaxWidth()
            )
        }

        item {
            Spacer(modifier = Modifier.height(16.dp))

            TextField(value = timeToCookState.value,
                onValueChange = { timeToCookState.value = it },
                label = { Text("Time to Cook") },
                modifier = Modifier.fillMaxWidth()
            )
        }

        item {
            Spacer(modifier = Modifier.height(16.dp))

            TextField(value = caloriesState.value,
                onValueChange = { caloriesState.value = it },
                label = { Text("Calories") },
                modifier = Modifier.fillMaxWidth()
            )
        }
        item {
            Spacer(modifier = Modifier.height(16.dp))

            // Ingredient List
            ingredientsList.forEachIndexed { index, ingredient ->
                TextField(value = ingredient,
                    onValueChange = { ingredientsList[index] = it },
                    label = { Text("Ingredient ${index + 1}") },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }

        item {
            Spacer(modifier = Modifier.height(16.dp))

            // Button to add new ingredient
            Button(onClick = {
                expanded = true
            }, modifier = Modifier.fillMaxWidth()) {
                Text("Add Ingredients To Recipe")
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

            // Button to finish adding recipe
            Button(onClick = {
                scope.launch {
                    snackbarHostState.showSnackbar("Ingredient added successfully!")
                }
            }, modifier = Modifier.fillMaxWidth()) {
                Text("Add Recipe")
            }
        }
    }
}

