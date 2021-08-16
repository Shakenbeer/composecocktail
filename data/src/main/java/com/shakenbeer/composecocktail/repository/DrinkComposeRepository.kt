package com.shakenbeer.composecocktail.repository

import androidx.annotation.WorkerThread
import com.shakenbeer.composecocktail.Error
import com.shakenbeer.composecocktail.Result
import com.shakenbeer.composecocktail.Success
import com.shakenbeer.composecocktail.connectivity.Connectivity
import com.shakenbeer.composecocktail.db.DrinkDao
import com.shakenbeer.composecocktail.entity.Drink
import com.shakenbeer.composecocktail.rest.TheCocktailDBService
import com.shakenbeer.composecocktail.rest.model.ApiDrink

class DrinkComposeRepository(
    private val drinkDao: DrinkDao,
    private val restService: TheCocktailDBService,
    private val connectivity: Connectivity
) : DrinkRepository {
    @WorkerThread
    override fun getDrinksByCategory(categoryName: String): Result<List<Drink>> {
        return when (val result =
            callApi(connectivity, restService.getDrinksByCategory(categoryName))) {
            is Success -> map(result.value.drinks)
            is Error -> result
        }
    }

    @WorkerThread
    override fun getDrinksByIngredient(ingredientName: String): Result<List<Drink>> {
        return when (val result =
            callApi(connectivity, restService.getDrinksByIngredient(ingredientName))) {
            is Success -> map(result.value.drinks)
            is Error -> result
        }
    }

    @WorkerThread
    override fun getFavoriteDrinks(): Result<List<Drink>> {
        return Success(
            drinkDao.getFavorites().map { Drink(it.id, it.name, it.thumbUrl) }
        )
    }

    private fun map(apiList: List<ApiDrink>?): Result<List<Drink>> {
        return if (apiList != null) {
            Success(
                apiList.asSequence()
                    .filter {
                        it.idDrink != null && it.strDrink != null
                    }
                    .map { Drink(it.idDrink!!, it.strDrink!!, it.strDrinkThumb ?: "") }
                    .toList()
            )
        } else {
            Success(emptyList())
        }
    }
}