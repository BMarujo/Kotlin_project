package com.example.kotlin_project.data

import kotlinx.coroutines.flow.Flow

interface RecipesRepository {

    fun getAllRecipes(): Flow<List<Recipe>>

    fun getRecipe(id: Int): Flow<Recipe?>

    suspend fun insertRecipe(recipe: Recipe)

    suspend fun deleteRecipe(recipe: Recipe)

    suspend fun updateRecipe(recipe: Recipe)

}