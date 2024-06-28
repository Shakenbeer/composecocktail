package com.shakenbeer.composecocktail.usecase

import com.shakenbeer.composecocktail.Success
import com.shakenbeer.composecocktail.entity.DetailedDrink
import com.shakenbeer.composecocktail.entity.DetailedDrink.Companion.NO_DETAILS
import com.shakenbeer.composecocktail.repository.DetailedDrinkRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.withContext
import javax.inject.Inject

class GetRandomDrinkUseCase @Inject constructor(private val detailedDrinkRepository: DetailedDrinkRepository) {

    val data = MutableStateFlow(NO_DETAILS)

    suspend fun execute(): DetailedDrink {
        val result = withContext(Dispatchers.IO) { detailedDrinkRepository.getRandomDrink() }
        if (result is Success) {
            data.value = result.value
        }
        return data.value
    }
}