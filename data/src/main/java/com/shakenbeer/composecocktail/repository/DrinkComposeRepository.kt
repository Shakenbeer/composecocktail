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
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class DrinkComposeRepository @Inject constructor(
    private val drinkDao: DrinkDao,
    private val restService: TheCocktailDBService,
    private val connectivity: Connectivity
) : DrinkRepository {
    @WorkerThread
    override fun getDrinksByCategory(categoryName: String): Flow<Result<List<Drink>>> {
        return callApi(connectivity) { restService.getDrinksByCategory(categoryName) }.map {
            when (it) {
                is Success -> map(it.value.drinks)
                is Error -> it
            }
        }
    }

    @WorkerThread
    override fun getDrinksByIngredient(ingredientName: String): Flow<Result<List<Drink>>> {
        return callApi(connectivity) { restService.getDrinksByIngredient(ingredientName) }.map {
            when (it) {
                is Success -> map(it.value.drinks)
                is Error -> it
            }
        }
    }

    @WorkerThread
    override fun getFavoriteDrinks(): Flow<Result<List<Drink>>> {
        return drinkDao.getFavorites().map { list -> Success(list.map { Drink(it.id, it.name, it.thumbUrl)  }) }

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