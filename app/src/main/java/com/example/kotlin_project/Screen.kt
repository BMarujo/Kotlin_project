package com.example.kotlin_project

import com.example.kotlin_project.data.Recipe
import kotlinx.serialization.json.Json
import java.net.URLEncoder
import java.nio.charset.StandardCharsets


sealed class Screen(val route: String) {
    object Inventory : Screen("inventory")
    object ItemConfiguration : Screen("item_configuration")

    object ChooseItemOrRecipe : Screen("choose_item_or_recipe")
    object AddItem : Screen("add_item")
    object AddRecipe : Screen("add_recipe")
    object SocialNetwork : Screen("social_network")
    object MainFragment2 : Screen("main_fragment2") {
        fun createRoute(recipe: Recipe): String {
            // Converte o objeto Recipe em uma string JSON para passar como argumento
            val recipeJson = Json.encodeToString(Recipe.serializer(), recipe)
            return "main_fragment2/$recipeJson"
        }
    }

    fun withArgs(vararg args: String): String {
        return buildString {
            append(route)
            args.forEach { arg ->
                append("/${URLEncoder.encode(arg, StandardCharsets.UTF_8.toString())}")
            }
        }
    }
}
