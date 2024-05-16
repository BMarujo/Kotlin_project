package com.example.kotlin_project

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import com.example.kotlin_project.data.RecipesRepository
import com.example.kotlin_project.data.Recipe
import com.example.kotlin_project.data.Ingredient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class RecipeViewModel(private val repository: RecipesRepository) : ViewModel() {

    private val _allRecipes: MutableStateFlow<List<Recipe>> = MutableStateFlow(emptyList())
    val allRecipes: StateFlow<List<Recipe>> = _allRecipes

    private val _allIngredients: MutableStateFlow<List<Ingredient>> = MutableStateFlow(emptyList())
    val allIngredients: StateFlow<List<Ingredient>> = _allIngredients

    init {
        observeRepositoryChanges()
    }

    private fun observeRepositoryChanges() {
        viewModelScope.launch {
            repository.getAllRecipes().collect { recipes ->
                _allRecipes.value = recipes
            }
        }

        viewModelScope.launch {
            repository.getAllIngredients().collect { ingredients ->
                _allIngredients.value = ingredients
            }
        }
    }

    fun insertRecipe(recipe: Recipe) {
        viewModelScope.launch {
            repository.insertRecipe(recipe)
        }
    }

    fun deleteRecipe(recipe: Recipe) {
        viewModelScope.launch {
            repository.deleteRecipe(recipe)
        }
    }

    fun updateRecipe(recipe: Recipe) {
        viewModelScope.launch {
            repository.updateRecipe(recipe)
        }
    }

    fun insertIngredient(ingredient: Ingredient) {
        viewModelScope.launch {
            repository.insertIngredient(ingredient)
        }
    }

    fun deleteIngredient(ingredient: Ingredient) {
        viewModelScope.launch {
            repository.deleteIngredient(ingredient)
        }
    }

    fun updateIngredient(ingredient: Ingredient) {
        viewModelScope.launch {
            repository.updateIngredient(ingredient)
        }
    }
}

