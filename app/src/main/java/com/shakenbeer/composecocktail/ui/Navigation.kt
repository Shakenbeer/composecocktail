package com.shakenbeer.composecocktail.ui

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.shakenbeer.composecocktail.R

sealed class Screen(val route: String, @StringRes val labelId: Int, @DrawableRes val iconId: Int) {
    object Categories : Screen("categories", R.string.bottom_nav_categories_title,
        R.drawable.ic_glass_cocktail_24dp)
    object Ingredients : Screen("ingredients", R.string.bottom_nav_ingredients_title,
        R.drawable.ic_fruit_cherries_24dp)
    object Favorites : Screen("favorites", R.string.bottom_nav_favorites_title,
        R.drawable.ic_favorite_24dp)
}

val topItems = listOf(Screen.Categories, Screen.Ingredients, Screen.Favorites)