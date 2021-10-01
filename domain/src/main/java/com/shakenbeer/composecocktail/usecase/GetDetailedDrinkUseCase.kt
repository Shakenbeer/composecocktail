package com.shakenbeer.composecocktail.usecase

import com.shakenbeer.composecocktail.Result
import com.shakenbeer.composecocktail.entity.DetailedDrink
import com.shakenbeer.composecocktail.repository.DetailedDrinkRepository
import javax.inject.Inject

class GetDetailedDrinkUseCase @Inject constructor(val detailedDrinkRepository: DetailedDrinkRepository) {

    fun execute(id: String): Result<DetailedDrink> {
        return detailedDrinkRepository.getDetailedDrink(id)
    }
}