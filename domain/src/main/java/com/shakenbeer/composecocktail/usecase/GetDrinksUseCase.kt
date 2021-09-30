package com.shakenbeer.composecocktail.usecase

import com.shakenbeer.composecocktail.GetDrinksParam
import com.shakenbeer.composecocktail.Result
import com.shakenbeer.composecocktail.entity.Drink
import com.shakenbeer.composecocktail.repository.DrinkRepository
import javax.inject.Inject

class GetDrinksUseCase @Inject constructor(private val drinkRepository: DrinkRepository) {

    fun execute(getDrinksParam: GetDrinksParam): Result<List<Drink>> {
        return when (getDrinksParam.type) {
            GetDrinksParam.Type.CATEGORY -> drinkRepository.getDrinksByCategory(getDrinksParam.value)
            GetDrinksParam.Type.INGREDIENT -> drinkRepository.getDrinksByIngredient(getDrinksParam.value)
            else -> drinkRepository.getFavoriteDrinks()
        }
    }
}