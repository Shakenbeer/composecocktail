package com.shakenbeer.composecocktail.repository

import com.shakenbeer.composecocktail.Result
import com.shakenbeer.composecocktail.entity.DetailedDrink

interface DetailedDrinkRepository {
    fun getDetailedDrink(id: String): Result<DetailedDrink>
    fun toggleFavorite(drink: DetailedDrink): Result<DetailedDrink>
    fun getRandomDrink(): Result<DetailedDrink>
}