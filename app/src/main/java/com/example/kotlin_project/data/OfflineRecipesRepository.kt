package com.example.kotlin_project.data

class OfflineRecipesRepository(
    private val recipeDao: RecipeDao,
    private val ingredientDao: IngredientDao // Add this parameter
) : RecipesRepository {

    override fun getAllRecipes() = recipeDao.getAllRecipes()

    override fun getRecipe(id: Int) = recipeDao.getRecipe(id)

    override suspend fun insertRecipe(recipe: com.example.kotlin_project.data.Recipe) = recipeDao.insert(recipe)

    override suspend fun deleteRecipe(recipe: com.example.kotlin_project.data.Recipe) = recipeDao.delete(recipe)

    override suspend fun updateRecipe(recipe: com.example.kotlin_project.data.Recipe) = recipeDao.update(recipe)

    override fun getAllIngredients() = ingredientDao.getAllIngredients()

    override fun getIngredient(id: Int) = ingredientDao.getIngredient(id)

    override suspend fun insertIngredient(ingredient: Ingredient) = ingredientDao.insert(ingredient)

    override suspend fun deleteIngredient(ingredient: Ingredient) = ingredientDao.delete(ingredient)

    override suspend fun updateIngredient(ingredient: Ingredient) = ingredientDao.update(ingredient)

    override fun getRecipeById(recipeId: Int) = recipeDao.getRecipeById(recipeId)

    override fun getIngredientsByNames(ingredientNames: List<String>) = ingredientDao.getIngredientsByNames(ingredientNames)
}
