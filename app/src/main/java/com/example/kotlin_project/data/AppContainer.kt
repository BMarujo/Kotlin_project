package com.example.kotlin_project.data

import android.content.Context

interface AppContainer {
     val recipesRepository: RecipesRepository
 }

class AppDataContainer(private val context: Context) : AppContainer {
    private val database = RecipesDatabase.getDatabase(context)

    override val recipesRepository: RecipesRepository by lazy {
        OfflineRecipesRepository(
            database.recipeDao(),
            database.ingredientDao() // Add this line
        )
    }
}
