package com.example.kotlin_project

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import com.example.kotlin_project.data.RecipesRepository
import com.example.kotlin_project.data.Recipe
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class RecipeViewModel(private val repository: RecipesRepository) : ViewModel() {

    private val _allRecipes: MutableStateFlow<List<Recipe>> = MutableStateFlow(emptyList())
    val allRecipes: StateFlow<List<Recipe>> = _allRecipes

    init {
        viewModelScope.launch {
            repository.getAllRecipes().collect { recipes ->
                _allRecipes.value = recipes
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
    private fun observeRepositoryChanges() {
        viewModelScope.launch {
            repository.getAllRecipes().collect { recipes ->
                _allRecipes.value = recipes
            }
        }
    }
}
