package com.example.kotlin_project.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface RecipeDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(item: Recipe)

    @Update
    suspend fun update(item: Recipe)

    @Delete
    suspend fun delete(item: Recipe)

    @Query("SELECT * from Recipes")
    fun getAllRecipes(): Flow<List<Recipe>>

    @Query("SELECT * from Recipes WHERE id = :id")
    fun getRecipe(id: Int): Flow<Recipe?>

    @Query("SELECT * FROM recipes WHERE id = :recipeId")
    fun getRecipeById(recipeId: Int): Flow<Recipe>
}