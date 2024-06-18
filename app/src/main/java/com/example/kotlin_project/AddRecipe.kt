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
import androidx.compose.runtime.collectAsState
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
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.example.kotlin_project.data.Recipe
import com.example.kotlin_project.util.StorageUtil.Companion.uploadToStorage
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.io.File
import java.io.IOException


@Composable
fun AddRecipePage(navController: NavController, scope: CoroutineScope, snackbarHostState: SnackbarHostState, context: Context = LocalContext.current, viewModel: RecipeViewModel) {
    val nameState = remember { mutableStateOf("") }
    val categoryState = remember { mutableStateOf("") }
    val descriptionState = remember { mutableStateOf("") }
    val ingredientsList = remember { mutableStateListOf<String>() }
    val timeToCookState = remember { mutableStateOf("") }
    val caloriesState = remember { mutableStateOf("") }
    var expanded by remember { mutableStateOf(false) }
    val result = remember { mutableStateOf<Uri?>(null) }

    val ingredientsState by viewModel.allIngredients.collectAsState()

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
                    // Dropdown menu for selecting ingredients
                    if (ingredientsState.isNotEmpty()) {
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(onClick = { expanded = true }, modifier = Modifier.fillMaxWidth()) {
                            Text("Add Ingredients To Recipe")
                        }

                        if (expanded) {
                            DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                                ingredientsState.forEach { ingredient ->
                                    DropdownMenuItem(onClick = {
                                        ingredientsList.add(ingredient.name)
                                        expanded = false
                                    }, text = { Text(ingredient.name) })
                                }
                            }
                        }
                    }
                }
        }

        item {

            // Button to finish adding recipe
            // Button click handler to finish adding the recipe
            Button(onClick = {
                // Ensure an image URI is selected
                    // Get the image URI from the local resources
                    val imageUri = saveImageToInternalStorage(context, result.value, nameState.value)

                    // Create a comma-separated string of ingredients
                    val ingredientsString = ingredientsList.joinToString(", ")

                    // Create a Recipe object with the entered details
                    val recipe = Recipe(
                        name = nameState.value,
                        category = categoryState.value,
                        description = descriptionState.value,
                        ingredients = ingredientsString, // Use the comma-separated string
                        timeToCook = timeToCookState.value.toIntOrNull() ?: 0,
                        calories = caloriesState.value.toIntOrNull() ?: 0,
                        imageUrl = imageUri.toString() // Store the image URI as a string
                    )

                    // Use the RecipeViewModel to insert the recipe into the database
                    viewModel.insertRecipe(recipe)

                    // Save the recipe to the Firebase
                    (context as ComponentActivity).lifecycleScope.launch {
                        val downloadUrl = imageUri?.let { uploadToStorage(it, context, "image") }
                        if (downloadUrl != null) {
                            //println("File uploaded successfully: $downloadUrl")

                            val db = FirebaseFirestore.getInstance()
                            val data = hashMapOf(
                                "name" to recipe.name,
                                "category" to recipe.category,
                                "description" to recipe.description,
                                "ingredients" to recipe.ingredients,
                                "timeToCook" to recipe.timeToCook,
                                "calories" to recipe.calories,
                                "image" to downloadUrl
                            )
                            db.collection("SocialNetworkOfRecipes").add(data)

                            scope.launch {
                                snackbarHostState.showSnackbar("Recipe Added Successfully!")
                            }
                        } else {
                            println("File upload failed")
                        }
                    }
            }, modifier = Modifier.fillMaxWidth()) {
                Text("Add Recipe")
            }



        }
    }
}

private fun saveImageToInternalStorage(context: Context, imageUri: Uri?, img_name: String): Uri? {
    if (imageUri != null) {
        try {
            val inputStream = context.contentResolver.openInputStream(imageUri)
            val fileName = img_name
            val outputStream = context.openFileOutput(fileName, Context.MODE_PRIVATE)
            inputStream?.copyTo(outputStream)
            inputStream?.close()
            outputStream.close()
            return Uri.fromFile(File(context.filesDir, fileName))
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }
    return null
}


