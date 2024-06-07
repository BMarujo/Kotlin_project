package com.example.kotlin_project

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import com.example.kotlin_project.data.RecipesRepository
import com.example.kotlin_project.data.Recipe
import com.example.kotlin_project.data.Ingredient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import android.speech.tts.TextToSpeech
import android.speech.tts.TextToSpeech.OnInitListener
import java.util.Locale

class RecipeViewModel(private val repository: RecipesRepository, private val context: android.content.Context) : ViewModel() {

    private val _allRecipes: MutableStateFlow<List<Recipe>> = MutableStateFlow(emptyList())
    val allRecipes: StateFlow<List<Recipe>> = _allRecipes

    private val _allIngredients: MutableStateFlow<List<Ingredient>> = MutableStateFlow(emptyList())
    val allIngredients: StateFlow<List<Ingredient>> = _allIngredients

    init {
        observeRepositoryChanges()
        initializeTextToSpeech()
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
    private val _recipe = MutableStateFlow<Recipe?>(null)
    val recipe: StateFlow<Recipe?> = _recipe.asStateFlow()

    private val _ingredients = MutableStateFlow<List<Ingredient>>(emptyList())
    val ingredients: StateFlow<List<Ingredient>> = _ingredients.asStateFlow()

    fun getRecipeById(recipeId: Int) {
        viewModelScope.launch {
            repository.getRecipeById(recipeId).collect { recipe ->
                _recipe.value = recipe
            }
        }
    }

    fun getIngredientsByNames(ingredientNames: List<String>) {
        viewModelScope.launch {
            repository.getIngredientsByNames(ingredientNames).collect { ingredients ->
                _ingredients.value = ingredients
            }
        }
    }
    private val searchQuery = MutableStateFlow("")

    val filteredRecipes: StateFlow<List<Recipe>> = combine(allRecipes, searchQuery) { recipes, query ->
        if (query.isBlank()) {
            recipes
        } else {
            recipes.filter { it.name.contains(query, ignoreCase = true) }
        }
    }.stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    fun setSearchQuery(query: String) {
        searchQuery.value = query
    }


    private lateinit var textToSpeech: TextToSpeech

    private fun initializeTextToSpeech() {
        textToSpeech = TextToSpeech(context, OnInitListener { status ->
            if (status != TextToSpeech.ERROR) {
                textToSpeech.language = Locale.UK
            }
        })
    }
    fun readDescription(description: String) {
        textToSpeech.speak(description, TextToSpeech.QUEUE_FLUSH, null, null)
    }

}

