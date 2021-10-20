package com.shakenbeer.composecocktail.usecase

import com.shakenbeer.composecocktail.Result
import com.shakenbeer.composecocktail.entity.Drink
import com.shakenbeer.composecocktail.repository.DrinkRepository
import javax.inject.Inject

class GetDrinksByCategoryUseCase @Inject constructor(private val drinkRepository: DrinkRepository) {

    operator fun invoke(category: String): Result<List<Drink>> =
        drinkRepository.getDrinksByCategory(category)
}