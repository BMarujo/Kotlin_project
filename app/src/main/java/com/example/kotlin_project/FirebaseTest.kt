/*package com.example.kotlin_project

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.kotlin_project.ui.theme.Kotlin_ProjectTheme
import com.google.firebase.firestore.FirebaseFirestore

class FirebaseActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Kotlin_ProjectTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    FirebaseTest()
                }
            }
        }
    }
}

@Composable
fun FirebaseTest() {
    var testData by remember { mutableStateOf("No data") }

    // Initialize Firestore
    val db = FirebaseFirestore.getInstance()

    // Write data to Firestore
    val data = hashMapOf(
        "teste1" to "Valor de teste 1",
        "teste2" to "Valor de teste 2"
    )

    db.collection("firebasetestconnection")
        .add(data)
        .addOnSuccessListener { documentReference ->
            testData = "Data added with ID: ${documentReference.id}"
        }
        .addOnFailureListener { e ->
            testData = "Error adding data: $e"
        }

    // Read data from Firestore
    db.collection("firebasetestconnection")
        .get()
        .addOnSuccessListener { result ->
            for (document in result) {
                testData = "${document.id} => ${document.data}"
            }
        }
        .addOnFailureListener { e ->
            testData = "Error getting data: $e"
        }

    // Display the test data
    Text(
        text = testData,
        modifier = Modifier.fillMaxSize()
    )
}

@Preview(showBackground = true)
@Composable
fun FirebaseTestPreview() {
    Kotlin_ProjectTheme {
        FirebaseTest()
    }
}
*/