package com.example.kotlin_project.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface IngredientDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(ingredient: Ingredient)

    @Update
    suspend fun update(ingredient: Ingredient)

    @Delete
    suspend fun delete(ingredient: Ingredient)

    @Query("SELECT * from Ingredients")
    fun getAllIngredients(): Flow<List<Ingredient>>

    @Query("SELECT * from Ingredients WHERE id = :id")
    fun getIngredient(id: Int): Flow<Ingredient?>

    @Query("SELECT * FROM ingredients WHERE name IN (:ingredientNames)")
    fun getIngredientsByNames(ingredientNames: List<String>): Flow<List<Ingredient>>
}
