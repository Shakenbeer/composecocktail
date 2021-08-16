package com.shakenbeer.composecocktail.usecase

import com.shakenbeer.composecocktail.Result
import com.shakenbeer.composecocktail.entity.Ingredient
import com.shakenbeer.composecocktail.repository.IngredientRepository
import javax.inject.Inject

class GetIngredientsUseCase @Inject constructor(
    private val ingredientRepository: IngredientRepository
) {
    fun execute(): Result<List<Ingredient>> {
        return ingredientRepository.getIngredients()
    }
}