package com.shakenbeer.composecocktail.repository

import com.shakenbeer.composecocktail.Result
import com.shakenbeer.composecocktail.entity.Drink
import kotlinx.coroutines.flow.Flow

interface DrinkRepository {
    fun getDrinksByCategory(categoryName: String): Flow<Result<List<Drink>>>
    fun getDrinksByIngredient(ingredientName: String): Flow<Result<List<Drink>>>
    fun getFavoriteDrinks(): Flow<Result<List<Drink>>>
}
