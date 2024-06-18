package com.example.kotlin_project

import android.Manifest
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.kotlin_project.data.AppContainer
import com.example.kotlin_project.data.AppDataContainer
import com.example.kotlin_project.data.RecipesRepository
import com.example.kotlin_project.data.model.TimerState
import com.example.kotlin_project.timer.TimerScreen
import com.example.kotlin_project.timer.TimerViewModel
import com.example.kotlin_project.ui.theme.Kotlin_ProjectTheme
import com.google.accompanist.insets.statusBarsPadding
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val appContainer: AppContainer by lazy {
        AppDataContainer(context = applicationContext)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Kotlin_ProjectTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background
                ) {
                    HomePage(appContainer.recipesRepository)
                }
            }
        }
        checkNotificationPermission(this)
    }
}


@Composable
fun HomePage(recipesRepository: RecipesRepository) {
    var selectedItem by remember { mutableIntStateOf(2) }
    val items = listOf("Sensors", "Recipes", "Home", "Inventory", "Add")
    val icons = listOf(
        Icons.Filled.Warning,
        Icons.Filled.Edit,
        Icons.Filled.AccountBox,
        Icons.Filled.List,
        Icons.Filled.Add
    )
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
    val timerViewModel: TimerViewModel = viewModel()
    val timerState by timerViewModel.timerState.observeAsState(TimerState())
    Scaffold(
        topBar = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .statusBarsPadding()
                    .padding(horizontal = 16.dp)
            ) {
                CircularButton(
                    R.drawable.baseline_home_24,
                    onClick = { selectedItem = 2 })
                Text(text = "Go To Home", style = MaterialTheme.typography.bodySmall)
            }
        },
        bottomBar = {
            NavigationBar {
                items.forEachIndexed { index, item ->
                    NavigationBarItem(icon = { Icon(icons[index], contentDescription = item) },
                        label = { Text(item, fontSize = 11.sp) },
                        selected = selectedItem == index,
                        onClick = { selectedItem = index },
                        colors = NavigationBarItemDefaults.colors(
                            indicatorColor = MaterialTheme.colorScheme.secondary,
                        ),
                    )
                }
            }
        },
        // para o temporizador ou para medir a temperatura
        floatingActionButton = {
            FloatingActionButton(onClick = {
                // redirect to the timer screen
                selectedItem = 5
            },
                containerColor = MaterialTheme.colorScheme.secondary) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_clock),
                    contentDescription = null,
                    tint = Color.Black,
                    modifier = Modifier.height(24.dp)
                )
            }
        },
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        },
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    bottom = it.calculateBottomPadding() // para evitar que o conteúdo fique atrás da barra de navegação
                )
                .padding(
                    top = it.calculateTopPadding()
                )
            //.padding(it) // <<-- or simply this
        ) {
            // Home Screen content
            val applicationContext = LocalContext.current
            val content = when (selectedItem) {
                0 -> AmbientTemperatureSensor()
                1 -> RecipesScreen(recipesRepository)
                2 -> HomeScreen(selectedItem = selectedItem, onItemSelected = { newItem ->
                    selectedItem = newItem
                })
                3 -> InventoryManagement( viewModel = RecipeViewModel(recipesRepository, context = applicationContext))
                4 -> AddPage(scope, snackbarHostState, recipesRepository)
                5 -> TimerScreen(modifier = Modifier.padding(it),
                    timerState = timerState,
                    timerActions = timerViewModel)
                else -> Text("Unknown screen")
            }

        }
    }
}


@Composable
fun HomeScreen(selectedItem: Int, onItemSelected: (Int) -> Unit) {
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Image(
            painter = painterResource(id = R.drawable.home_kotlinproject_icm),
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
                                    Color.Transparent, Color.White
                                ), startY = 500f, endY = 0f
                            ), RoundedCornerShape(48, 48, 48, 16)
                        )
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(
                                    Color.Transparent, Color.White
                                ), startY = 500f, endY = 0f
                            ), RoundedCornerShape(48, 48, 48, 16)
                        )
                        .fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Text(
                            text = "Hi, User X!",
                            textAlign = TextAlign.Left,
                            fontWeight = FontWeight.Bold,
                            fontSize = MaterialTheme.typography.titleLarge.fontSize,
                        )
                        Text(
                            text = "Explore your inventory, favorite recipes and more",
                            textAlign = TextAlign.Left,
                            fontWeight = FontWeight.Bold,
                        )
                    }
                }

                // Lista de itens clicáveis
                Spacer(modifier = Modifier.height(16.dp))
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(6.dp),
                    verticalArrangement = Arrangement.spacedBy(2.dp),
                ) {
                    // List Item
                    Card(
// blur background
                        modifier = Modifier
                            .padding(16.dp)
                            .border(
                                2.dp, Brush.horizontalGradient(
                                    colors = listOf(
                                        Color.Transparent, Color.White
                                    ), startX = 1500f, endX = 0f
                                ), RoundedCornerShape(16.dp)
                            )
                            .background(
                                Brush.horizontalGradient(
                                    colors = listOf(
                                        Color.Transparent, Color.White
                                    ), startX = 1500f, endX = 0f
                                ), RoundedCornerShape(16.dp)
                            ),
                        //.height(120.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = Color.Transparent,
                        ),
                    ) {
                        ListItem(colors = ListItemDefaults.colors(
                            //transparent
                            containerColor = Color.Transparent,
                            headlineColor = Color.Black,
                            leadingIconColor = Color.Black,
                            overlineColor = Color.Black,
                            supportingColor = Color.Black,
                            trailingIconColor = Color.Black,
                        ), headlineContent = {
                            Text(
                                text = "Inventory",
                                fontWeight = FontWeight.Bold,
                            )
                        }, supportingContent = {
                            Text(
                                text = "Add, edit and remove items/ingredients from your inventory",
                                textAlign = TextAlign.Left,
                            )
                        }, leadingContent = {
                            Icon(
                                Icons.Filled.Favorite,
                                contentDescription = "Localized description",
                            )
                        }, trailingContent = {
                            Button(
                                onClick = { onItemSelected(3) },
                                content = { Text("Explore more") },
                                modifier = Modifier.align(Alignment.CenterHorizontally),
                                /*colors = ButtonDefaults.buttonColors(
                                    containerColor = Color.White, contentColor = Color.Black
                                )*/
                            )
                        })
                    }
                    Card(
// blur background
                        modifier = Modifier
                            .padding(16.dp)
                            .border(
                                2.dp, Brush.horizontalGradient(
                                    colors = listOf(
                                        Color.Transparent, Color.White
                                    ), startX = 1500f, endX = 0f
                                ), RoundedCornerShape(16.dp)
                            )
                            .background(
                                Brush.horizontalGradient(
                                    colors = listOf(
                                        Color.Transparent, Color.White
                                    ), startX = 1500f, endX = 0f
                                ), RoundedCornerShape(16.dp)
                            ),
                        //.height(120.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = Color.Transparent,
                        ),
                    ) {
                        ListItem(colors = ListItemDefaults.colors(
                            //transparent
                            containerColor = Color.Transparent,
                            headlineColor = Color.Black,
                            leadingIconColor = Color.Black,
                            overlineColor = Color.Black,
                            supportingColor = Color.Black,
                            trailingIconColor = Color.Black,
                        ), headlineContent = {
                            Text(
                                text = "Check Our Sensors!",
                                fontWeight = FontWeight.Bold,
                            )
                        }, supportingContent = {
                            Text(
                                text = "Check the temperature and light sensors",
                                textAlign = TextAlign.Left,
                            )
                        }, leadingContent = {
                            Icon(
                                Icons.Filled.Favorite,
                                contentDescription = "Localized description",
                            )
                        }, trailingContent = {
                            Button(
                                onClick = { onItemSelected(0) },
                                content = { Text("Explore more") },
                                modifier = Modifier.align(Alignment.CenterHorizontally),
                                /*colors = ButtonDefaults.buttonColors(
                                    containerColor = Color.White, contentColor = Color.Black
                                )*/
                            )
                        })
                    }
                    Card(
// blur background
                        modifier = Modifier
                            .padding(16.dp)
                            .border(
                                2.dp, Brush.horizontalGradient(
                                    colors = listOf(
                                        Color.Transparent, Color.White
                                    ), startX = 1500f, endX = 0f
                                ), RoundedCornerShape(16.dp)
                            )
                            .background(
                                Brush.horizontalGradient(
                                    colors = listOf(
                                        Color.Transparent, Color.White
                                    ), startX = 1500f, endX = 0f
                                ), RoundedCornerShape(16.dp)
                            ),
                        //.height(120.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = Color.Transparent,
                        ),
                    ) {
                        ListItem(colors = ListItemDefaults.colors(
                            //transparent
                            containerColor = Color.Transparent,
                            headlineColor = Color.Black,
                            leadingIconColor = Color.Black,
                            overlineColor = Color.Black,
                            supportingColor = Color.Black,
                            trailingIconColor = Color.Black,
                        ), headlineContent = {
                            Text(
                                text = "Recipes",
                                fontWeight = FontWeight.Bold,
                            )
                        }, supportingContent = {
                            Text(
                                text = "Save your recipes and add new ones",
                                textAlign = TextAlign.Left,
                                modifier = Modifier.fillMaxSize()
                            )
                        }, leadingContent = {
                            Icon(
                                Icons.Filled.Favorite,
                                contentDescription = "Localized description",
                            )
                        }, trailingContent = {
                            Button(
                                onClick = { onItemSelected(1) },
                                content = { Text("Explore more") },
                                modifier = Modifier.align(Alignment.CenterHorizontally),
                                /*colors = ButtonDefaults.buttonColors(
                                    containerColor = Color.White, contentColor = Color.Black
                                )*/
                            )
                        })
                    }
                    Card(
// blur background
                        modifier = Modifier
                            .padding(16.dp)
                            .border(
                                2.dp, Brush.horizontalGradient(
                                    colors = listOf(
                                        Color.Transparent, Color.White
                                    ), startX = 1500f, endX = 0f
                                ), RoundedCornerShape(16.dp)
                            )
                            .background(
                                Brush.horizontalGradient(
                                    colors = listOf(
                                        Color.Transparent, Color.White
                                    ), startX = 1500f, endX = 0f
                                ), RoundedCornerShape(16.dp)
                            ),
                        //.height(120.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = Color.Transparent,
                        ),
                    ) {
                        ListItem(colors = ListItemDefaults.colors(
                            //transparent
                            containerColor = Color.Transparent,
                            headlineColor = Color.Black,
                            leadingIconColor = Color.Black,
                            overlineColor = Color.Black,
                            supportingColor = Color.Black,
                            trailingIconColor = Color.Black,
                        ), headlineContent = {
                            Text(
                                text = "Add Ingredients",
                                fontWeight = FontWeight.Bold,
                            )
                        }, supportingContent = {
                            Text(
                                text = "Add new ingredients to your inventory",
                                textAlign = TextAlign.Left,
                            )
                        }, leadingContent = {
                            Icon(
                                Icons.Filled.Favorite,
                                contentDescription = "Localized description",
                            )
                        }, trailingContent = {
                            Button(
                                onClick = { onItemSelected(4) },
                                content = { Text("Explore more") },
                                modifier = Modifier.align(Alignment.CenterHorizontally),
                                /*colors = ButtonDefaults.buttonColors(
                                    containerColor = Color.White, contentColor = Color.Black
                                )*/
                            )
                        })
                    }


                    Card(
// blur background
                        modifier = Modifier
                            .padding(16.dp)
                            .border(
                                2.dp, Brush.horizontalGradient(
                                    colors = listOf(
                                        Color.Transparent, Color.White
                                    ), startX = 1500f, endX = 0f
                                ), RoundedCornerShape(16.dp)
                            )
                            .background(
                                Brush.horizontalGradient(
                                    colors = listOf(
                                        Color.Transparent, Color.White
                                    ), startX = 1500f, endX = 0f
                                ), RoundedCornerShape(16.dp)
                            ),
                        //.height(120.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = Color.Transparent,
                        ),
                    ) {
                        ListItem(colors = ListItemDefaults.colors(
                            //transparent
                            containerColor = Color.Transparent,
                            headlineColor = Color.Black,
                            leadingIconColor = Color.Black,
                            overlineColor = Color.Black,
                            supportingColor = Color.Black,
                            trailingIconColor = Color.Black,
                        ), headlineContent = {
                            Text(
                                text = "Add Recipes",
                                fontWeight = FontWeight.Bold,
                            )
                        }, supportingContent = {
                            Text(
                                text = "Add new recipes to your inventory",
                                textAlign = TextAlign.Left,
                            )
                        }, leadingContent = {
                            Icon(
                                Icons.Filled.Favorite,
                                contentDescription = "Localized description",
                            )
                        }, trailingContent = {
                            Button(
                                onClick = { onItemSelected(4) },
                                content = { Text("Explore more") },
                                modifier = Modifier.align(Alignment.CenterHorizontally),
                                /*colors = ButtonDefaults.buttonColors(
                                    containerColor = Color.White, contentColor = Color.Black
                                )*/
                            )
                        })
                    }
                    /*Card(
// blur background
                        modifier = Modifier
                            .padding(16.dp)
                            .border(
                                2.dp, Brush.horizontalGradient(
                                    colors = listOf(
                                        Color.Transparent, Color.White
                                    ), startX = 1500f, endX = 0f
                                ), RoundedCornerShape(16.dp)
                            )
                            .background(
                                Brush.horizontalGradient(
                                    colors = listOf(
                                        Color.Transparent, Color.White
                                    ), startX = 1500f, endX = 0f
                                ), RoundedCornerShape(16.dp)
                            ),
                        //.height(120.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = Color.Transparent,
                        ),
                    ) {
                        ListItem(colors = ListItemDefaults.colors(
                            //transparent
                            containerColor = Color.Transparent,
                            headlineColor = Color.Black,
                            leadingIconColor = Color.Black,
                            overlineColor = Color.Black,
                            supportingColor = Color.Black,
                            trailingIconColor = Color.Black,
                        ), headlineContent = {
                            Text(
                                text = "Favorites",
                                fontWeight = FontWeight.Bold,
                            )
                        }, supportingContent = {
                            Text(
                                text = "See your favorite recipes and add new ones",
                                textAlign = TextAlign.Left,
                            )
                        }, leadingContent = {
                            Icon(
                                Icons.Filled.Favorite,
                                contentDescription = "Localized description",
                            )
                        }, trailingContent = {
                            Button(
                                onClick = { /*onItemSelected(0)*/ },
                                content = { Text("Explore more") },
                                modifier = Modifier.align(Alignment.CenterHorizontally),
                                /*colors = ButtonDefaults.buttonColors(
                                    containerColor = Color.White, contentColor = Color.Black
                                )*/
                            )
                        })
                    }*/
                }
            }
        }
    }
}


@Composable
fun AmbientTemperatureSensor() {
    val context = LocalContext.current
    var temperature by remember { mutableStateOf("0") }
    var isSensorRunning by remember { mutableStateOf(false) }
    var sensorAvailable by remember { mutableStateOf(true) }
    val sensorManager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
    val temperatureSensor = sensorManager.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE)

    val sensorEventListener = remember {
        object : SensorEventListener {
            override fun onSensorChanged(event: SensorEvent?) {
                if (event?.sensor?.type == Sensor.TYPE_AMBIENT_TEMPERATURE) {
                    temperature = event.values[0].toString()
                    Log.d("AmbientTemperatureSensor", "Temperature: $temperature")
                }
            }

            override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}
        }
    }

    fun startSensor() {
        if (ContextCompat.checkSelfPermission(
                context, Manifest.permission.BODY_SENSORS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // Request permission
            ActivityCompat.requestPermissions(
                context as ComponentActivity, arrayOf(Manifest.permission.BODY_SENSORS), 0
            )
        } else {
            if (temperatureSensor == null) {
                sensorAvailable = false
            } else {
                sensorManager.registerListener(
                    sensorEventListener, temperatureSensor, SensorManager.SENSOR_DELAY_NORMAL
                )
                isSensorRunning = true
            }
        }
    }

    fun stopSensor() {
        sensorManager.unregisterListener(sensorEventListener)
        isSensorRunning = false
    }

    val context2 = LocalContext.current
    var temperature2 by remember { mutableStateOf("0") }
    val temperatureAsDouble: Double? = temperature2.toDoubleOrNull()
    var isSensorRunning2 by remember { mutableStateOf(false) }
    var sensorAvailable2 by remember { mutableStateOf(true) }
    val sensorManager2 = context2.getSystemService(Context.SENSOR_SERVICE) as SensorManager
    val temperatureSensor2 = sensorManager2.getDefaultSensor(Sensor.TYPE_LIGHT)

    val sensorEventListener2 = remember {
        object : SensorEventListener {
            override fun onSensorChanged(event: SensorEvent?) {
                if (event?.sensor?.type == Sensor.TYPE_LIGHT) {
                    temperature2 = event.values[0].toString()
                    Log.d("LightSensor", "Light: $temperature2")
                }
            }

            override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}
        }
    }

    fun startSensor2() {
        if (ContextCompat.checkSelfPermission(
                context2, Manifest.permission.BODY_SENSORS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // Request permission
            ActivityCompat.requestPermissions(
                context2 as ComponentActivity, arrayOf(Manifest.permission.BODY_SENSORS), 0
            )
        } else {
            if (temperatureSensor2 == null) {
                sensorAvailable2 = false
            } else {
                sensorManager2.registerListener(
                    sensorEventListener2, temperatureSensor2, SensorManager.SENSOR_DELAY_NORMAL
                )
                isSensorRunning2 = true
            }
        }
    }

    fun stopSensor2() {
        sensorManager2.unregisterListener(sensorEventListener2)
        isSensorRunning2 = false
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier.fillMaxSize()
    ) {
        if (sensorAvailable) {
            Text(text = "Ambient Temperature: $temperature °C")
            Spacer(modifier = Modifier.height(20.dp))
            Button(onClick = {
                if (isSensorRunning) {
                    stopSensor()
                } else {
                    startSensor()
                }
            }) {
                Text(if (isSensorRunning) "Stop Sensor" else "Start Sensor")
            }
        } else {
            Text(text = "Ambient Temperature Sensor not available on this device.")
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (sensorAvailable2) {
            Text(text = "Light: $temperature2")
            Spacer(modifier = Modifier.height(20.dp))
            if (temperatureAsDouble != null) {
                if (temperatureAsDouble == 0.0) {
                    Text(text = "No light detected", textAlign = TextAlign.Center, color = Color.Red)
                }
            }
            if (temperatureAsDouble != null) {
                if (temperatureAsDouble <= 300 && temperatureAsDouble > 0) {
                    Text(text = "Ensure you have proper lighting to handle sharp objects and hot surfaces", textAlign = TextAlign.Center, color = Color.Red)
                }
            }
            Button(onClick = {
                if (isSensorRunning2) {
                    stopSensor2()
                } else {
                    startSensor2()
                }
            }) {
                Text(if (isSensorRunning2) "Stop Sensor" else "Start Sensor")
            }
        } else {
            Text(text = "Light Sensor not available on this device.")
        }
    }
}

fun checkNotificationPermission(context: Context) {
    val areNotificationsEnabled = NotificationManagerCompat.from(context).areNotificationsEnabled()

    if (!areNotificationsEnabled) {
        AlertDialog.Builder(context)
            .setTitle("Enable Notifications")
            .setMessage("Please enable notifications for our app in your system settings.")
            .setPositiveButton("Go to settings") { _, _ ->
                val intent = Intent(Settings.ACTION_APP_NOTIFICATION_SETTINGS).apply {
                    putExtra(Settings.EXTRA_APP_PACKAGE, context.packageName)
                }
                context.startActivity(intent)
            }
            .setNegativeButton("Cancel", null)
            .show()
    }
}

/*
@Preview(showBackground = true)
@Composable
fun FavoritesPreview() {
    Kotlin_ProjectTheme {
        FavoritesScreen()
    }
}

@Preview(showBackground = true)
@Composable
fun RecipesPreview() {
    Kotlin_ProjectTheme {
        RecipesScreen()
    }
}
*/

/*
@Preview(showBackground = true)
@Composable
fun AddPreview() {
    Kotlin_ProjectTheme {
        AddScreen()
    }
}*/