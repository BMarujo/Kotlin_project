package com.example.kotlin_project.data

class OfflineRecipesRepository(private val recipeDao: RecipeDao): RecipesRepository {

    override fun getAllRecipes() = recipeDao.getAllRecipes()

    override fun getRecipe(id: Int) = recipeDao.getRecipe(id)

    override suspend fun insertRecipe(recipe: com.example.kotlin_project.data.Recipe) = recipeDao.insert(recipe)

    override suspend fun deleteRecipe(recipe: com.example.kotlin_project.data.Recipe) = recipeDao.delete(recipe)

    override suspend fun updateRecipe(recipe: com.example.kotlin_project.data.Recipe) = recipeDao.update(recipe)
}