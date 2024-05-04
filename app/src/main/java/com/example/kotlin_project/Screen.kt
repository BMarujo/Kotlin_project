package com.example.kotlin_project

sealed class Screen (val route: String){
    object Inventory: Screen("inventory")
    object ItemConfiguration: Screen("item_configuration")

    object AddItem: Screen("add_item")

    fun withArgs(vararg args: String): String {
        return buildString {
            append(route)
            args.forEach { arg ->
                append("/$arg")
            }
        }
    }
}