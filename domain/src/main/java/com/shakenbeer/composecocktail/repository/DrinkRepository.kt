package com.shakenbeer.composecocktail.repository

import com.shakenbeer.composecocktail.Result
import com.shakenbeer.composecocktail.entity.Drink

interface DrinkRepository {
    fun getDrinksByCategory(categoryName: String): Result<List<Drink>>
    fun getDrinksByIngredient(ingredientName: String): Result<List<Drink>>
    fun getFavoriteDrinks(): Result<List<Drink>>
}
