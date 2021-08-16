package com.shakenbeer.composecocktail.usecase

import com.shakenbeer.composecocktail.Result
import com.shakenbeer.composecocktail.entity.DetailedDrink
import com.shakenbeer.composecocktail.repository.DetailedDrinkRepository

class GetDetailedDrinkUseCase(val detailedDrinkRepository: DetailedDrinkRepository) {

    fun execute(id: String): Result<DetailedDrink> {
        return detailedDrinkRepository.getDetailedDrink(id)
    }
}