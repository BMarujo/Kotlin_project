package com.example.kotlin_project.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable

@Serializable
@Entity(tableName = "Recipes")
data class Recipe(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String,
    val category: String,
    val ingredients: String,
    val timeToCook: Int,
    val calories: Int,
    val description: String,
    val imageUrl: String
)