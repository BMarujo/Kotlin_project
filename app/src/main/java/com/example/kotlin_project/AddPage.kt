package com.example.kotlin_project

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Bitmap.CompressFormat
import android.net.Uri
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.example.kotlin_project.data.Ingredient
import com.example.kotlin_project.data.RecipesRepository
import com.example.kotlin_project.util.StorageUtil
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream
import java.io.IOException


@Composable
fun AddPage(scope: CoroutineScope, snackbarHostState: SnackbarHostState, recipesRepository: RecipesRepository){

    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = Screen.ChooseItemOrRecipe.route){
        composable(route = Screen.ChooseItemOrRecipe.route){
            ChooseItemOrRecipeScreen(navController)
        }

        composable(route = Screen.AddItem.route){
            AddItemScreen(navController, scope, snackbarHostState,  viewModel = RecipeViewModel(recipesRepository, context = LocalContext.current))
        }

        composable(route = Screen.AddRecipe.route){
            AddRecipePage(navController, scope, snackbarHostState, viewModel = RecipeViewModel(recipesRepository, context = LocalContext.current))
        }

        composable(route = Screen.Inventory.route){
            InventoryManagement(viewModel = RecipeViewModel(recipesRepository, context = LocalContext.current))
        }
    }
}

@Composable
fun ChooseItemOrRecipeScreen(navController: NavHostController) {
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Image(
            painter = painterResource(id = R.drawable.inventoryscreen_background),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )
        Column (
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ){
            Button(onClick = { navController.navigate(Screen.AddItem.route) }) {
            Text("Add Item")
            }
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = { navController.navigate(Screen.AddRecipe.route) }) {
                Text("Add Recipe")
            }
        }
    }
}
@Composable
fun AddItemScreen(navController: NavController, scope: CoroutineScope, snackbarHostState: SnackbarHostState, context: Context = LocalContext.current, viewModel: RecipeViewModel) {
    val newtitle = remember { mutableStateOf("") }
    val newquantity = remember { mutableStateOf("") }
    val newunit = remember { mutableStateOf("") }

    val result = remember { mutableStateOf<Uri?>(null) }
    // Function to save the ingredient information and image locally
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(16.dp),
        contentPadding = PaddingValues(top = 0.dp), state = rememberLazyListState(),
    ) {
        item {
            // Other UI components...
            Button(onClick = { navController.navigate(Screen.ChooseItemOrRecipe.route) }) {
                Text("Go Back")
            }
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Details of the New Ingredient",
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
            Spacer(modifier = Modifier.height(16.dp))
            TextField(
                value = newtitle.value,
                onValueChange = { newtitle.value = it },
                label = { Text("New Ingredient Name") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))
            TextField(
                value = newquantity.value.toString(),
                onValueChange = { newquantity.value = it },
                label = { Text("New Ingredient Quantity") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))
            TextField(
                value = newunit.value,
                onValueChange = { newunit.value = it },
                label = { Text("Unit of Measurement") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick ={if (newtitle.value.isNotBlank() && newquantity.value.toDouble() > 0 && newunit.value.isNotBlank() ) {

                    val imageUri = saveImageToInternalStorage(context, result.value, newtitle.value)
                    val ingredient = Ingredient(
                        name = newtitle.value,
                        quantity = newquantity.value.toDouble(),
                        measurement = newunit.value,
                        imageUrl = imageUri.toString() // Convert Uri to string for storage
                    )

                    // Save the ingredient in the database
                    viewModel.insertIngredient(ingredient)

                    // Save the ingredient in the Firebase
                    (context as ComponentActivity).lifecycleScope.launch {
                        val downloadUrl = imageUri?.let {
                            StorageUtil.uploadToStorage(
                                it,
                                context,
                                "image"
                            )
                        }
                        if (downloadUrl != null) {
                            //println("File uploaded successfully: $downloadUrl")

                            val db = FirebaseFirestore.getInstance()
                            val data = hashMapOf(
                                "name" to ingredient.name,
                                "quantity" to ingredient.quantity,
                                "measurement" to ingredient.measurement,
                                "imageUrl" to downloadUrl
                            )
                            db.collection("IngredientsData").add(data)

                            // Show a snackbar to indicate that the ingredient was added successfully
                            scope.launch {
                                snackbarHostState.showSnackbar("Ingredient added successfully!")
                            }
                        } else {
                            println("File upload failed")
                        }
                    }

                } else {
                    // Show an error message if any field is empty
                    scope.launch {
                        snackbarHostState.showSnackbar("Please fill in all fields and select an image.")
                    }
                } }, // Call the saveIngredient function when the button is clicked
                modifier = Modifier.fillMaxWidth(),
            ) {
                Text("Save Ingredient")
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
// IDK if its working
/*
fun saveImageToLocal(context: Context, uri: Uri): Uri? {
    val imagesDir = File(context.filesDir, "images")
    if (!imagesDir.exists()) {
        imagesDir.mkdirs()
    }

    val imageFile = File(imagesDir, "my_image.jpg")
    val imageUri = FileProvider.getUriForFile(context, context.packageName + ".provider", imageFile)

    try {
        val bitmap = ImageDecoder.decodeBitmap(ImageDecoder.createSource(context.contentResolver, uri))
        val outputStream: OutputStream = FileOutputStream(imageFile)
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
        outputStream.flush()
        outputStream.close()
        return imageUri
    } catch (e: IOException) {
        e.printStackTrace()
    }
    return null
}
 */

fun Bitmap.toUri(context: Context): Uri? {
    val imagesDir = File(context.cacheDir, "images")
    imagesDir.mkdirs()
    val imageFile = File.createTempFile("selected_image_", ".jpg", imagesDir)
    return try {
        val outputStream = FileOutputStream(imageFile)
        compress(CompressFormat.JPEG, 100, outputStream)
        outputStream.flush()
        outputStream.close()
        FileProvider.getUriForFile(context, "${context.packageName}.fileprovider", imageFile)
    } catch (e: IOException) {
        e.printStackTrace()
        null
    }
}

private const val REQUEST_CAMERA_PERMISSION = 123

fun requestCameraPermission(activity: ComponentActivity, onPermissionGranted: () -> Unit) {
    val context = activity.applicationContext
    val permission = Manifest.permission.CAMERA
    when {
        ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED -> {
            // Permissão já concedida, chame onPermissionGranted imediatamente
            onPermissionGranted()
        }
        ActivityCompat.shouldShowRequestPermissionRationale(activity, permission) -> {
            // Exiba uma explicação sobre por que a permissão é necessária e solicite-a novamente se necessário
            // Você pode exibir um diálogo ou mensagem explicando a necessidade da permissão aqui
            ActivityCompat.requestPermissions(activity, arrayOf(permission), REQUEST_CAMERA_PERMISSION)
        }
        else -> {
            // Solicitar a permissão diretamente
            ActivityCompat.requestPermissions(activity, arrayOf(permission), REQUEST_CAMERA_PERMISSION)
        }
    }
}





