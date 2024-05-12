package com.example.kotlin_project.data

import android.content.Context

interface AppContainer {
     val recipesRepository: RecipesRepository
 }

class AppDataContainer (private val context: Context): AppContainer {
     override val recipesRepository: RecipesRepository by lazy {
         OfflineRecipesRepository(RecipesDatabase.getDatabase(context).recipeDao())
     }
 }