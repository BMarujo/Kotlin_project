package com.example.kotlin_project.data

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import android.content.Context

@Database(entities = [Recipe::class, Ingredient::class], version = 1, exportSchema = false)
abstract class RecipesDatabase : RoomDatabase() {
    abstract fun recipeDao(): RecipeDao
    abstract fun ingredientDao(): IngredientDao // Add this line

    companion object {
        @Volatile
        private var Instance: RecipesDatabase? = null

        fun getDatabase(context: Context): RecipesDatabase {
            return Instance ?: synchronized(this) {
                Room.databaseBuilder(context, RecipesDatabase::class.java, "recipes_database")
                    .fallbackToDestructiveMigration()
                    .build()
                    .also { Instance = it }
            }
        }
    }
}
