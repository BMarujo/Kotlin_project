package com.example.kotlin_project
import kotlinx.serialization.Serializable

@Serializable
data class IngredientData(
    val name: String = "",
    val quantity: Double = 0.0,
    val imageUrl: String = ""
) {
    // Construtor sem argumentos
    constructor() : this("", 0.0, "")
}