package com.example.kotlin_project.data

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "Ingredients")
data class Ingredient(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String,
    val quantity: Double,
    val measurement: String,
    val imageUrl: String // assuming each ingredient can have an associated photo
)

