package com.shakenbeer.composecocktail.usecase

import com.shakenbeer.composecocktail.Result
import com.shakenbeer.composecocktail.entity.Drink
import com.shakenbeer.composecocktail.repository.DrinkRepository
import javax.inject.Inject

class GetDrinksByIngredientUseCase @Inject constructor(private val drinkRepository: DrinkRepository) {

    operator fun invoke(ingredient: String): Result<List<Drink>> =
        drinkRepository.getDrinksByIngredient(ingredient)
}