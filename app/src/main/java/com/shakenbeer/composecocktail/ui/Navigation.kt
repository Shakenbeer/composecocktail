package com.shakenbeer.composecocktail.ui

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.shakenbeer.composecocktail.R

const val categories = "categories"
const val ingredients = "ingredients"
const val favorites = "favorites"
const val name = "name"

sealed class Screen(val route: String) {

    sealed class Tab(
        route: String,
        @StringRes val labelId: Int = 0,
        @DrawableRes val iconId: Int = 0
    ) :
        Screen(route) {


        object Categories : Tab(
            categories, R.string.bottom_nav_categories_title,
            R.drawable.ic_glass_cocktail_24dp
        )

        object Ingredients : Tab(
            ingredients, R.string.bottom_nav_ingredients_title,
            R.drawable.ic_fruit_cherries_24dp
        )

        object Favorites : Tab(
            favorites, R.string.bottom_nav_favorites_title,
            R.drawable.ic_favorite_24dp
        )
    }

    sealed class Drinks(private val parent: String) : Screen("$parent/{$name}") {
        object ByCategory : Drinks(categories)
        object ByIngredient : Drinks(ingredients)

        fun route(param: String): String {
            return "$parent/${param.deslash()}"
        }
    }
}

private const val slashKeeper = "439972"
fun String.deslash(): String = this.replace("/", slashKeeper)
fun String.slash(): String = this.replace(slashKeeper, "/")


val topItems = listOf(Screen.Tab.Categories, Screen.Tab.Ingredients, Screen.Tab.Favorites)