package com.shakenbeer.composecocktail.usecase

import com.shakenbeer.composecocktail.entity.DetailedDrink
import com.shakenbeer.composecocktail.repository.DetailedDrinkRepository
import javax.inject.Inject

class MakeFavoriteDrinkUseCase @Inject constructor(
    private val detailedDrinkRepository: DetailedDrinkRepository
) {
    fun execute(drink: DetailedDrink) = detailedDrinkRepository.toggleFavorite(drink)
}