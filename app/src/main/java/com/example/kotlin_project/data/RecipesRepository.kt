package com.example.kotlin_project.data

import kotlinx.coroutines.flow.Flow

interface RecipesRepository {

    fun getAllRecipes(): Flow<List<Recipe>>

    fun getRecipe(id: Int): Flow<Recipe?>

    suspend fun insertRecipe(recipe: Recipe)

    suspend fun deleteRecipe(recipe: Recipe)

    suspend fun updateRecipe(recipe: Recipe)

    fun getAllIngredients(): Flow<List<Ingredient>>

    fun getIngredient(id: Int): Flow<Ingredient?>

    suspend fun insertIngredient(ingredient: Ingredient)

    suspend fun deleteIngredient(ingredient: Ingredient)

    suspend fun updateIngredient(ingredient: Ingredient)
    fun getRecipeById(recipeId: Int): Flow<Recipe>
    fun getIngredientsByNames(ingredientNames: List<String>): Flow<List<Ingredient>>
}


