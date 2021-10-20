package com.shakenbeer.composecocktail.usecase

import com.shakenbeer.composecocktail.Result
import com.shakenbeer.composecocktail.entity.Drink
import com.shakenbeer.composecocktail.repository.DrinkRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetFavoritesDrinksUseCase @Inject constructor(private val drinkRepository: DrinkRepository) {

    operator fun invoke(): Flow<Result<List<Drink>>> = drinkRepository.getFavoriteDrinks()
}