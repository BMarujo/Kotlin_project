package com.example.kotlin_project

import java.net.URLEncoder
import java.nio.charset.StandardCharsets

sealed class Screen(val route: String) {
    object Inventory : Screen("inventory")
    object ItemConfiguration : Screen("item_configuration")

    object ChooseItemOrRecipe : Screen("choose_item_or_recipe")
    object AddItem : Screen("add_item")
    object AddRecipe : Screen("add_recipe")

    fun withArgs(vararg args: String): String {
        return buildString {
            append(route)
            args.forEach { arg ->
                append("/${URLEncoder.encode(arg, StandardCharsets.UTF_8.toString())}")
            }
        }
    }
}
