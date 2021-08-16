package com.shakenbeer.composecocktail.repository

import com.shakenbeer.composecocktail.Result
import com.shakenbeer.composecocktail.entity.Ingredient

interface IngredientRepository {
    fun getIngredients(): Result<List<Ingredient>>
}
