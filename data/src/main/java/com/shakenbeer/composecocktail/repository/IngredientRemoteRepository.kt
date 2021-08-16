package com.shakenbeer.composecocktail.repository

import com.shakenbeer.composecocktail.Error
import com.shakenbeer.composecocktail.Result
import com.shakenbeer.composecocktail.Success
import com.shakenbeer.composecocktail.connectivity.Connectivity
import com.shakenbeer.composecocktail.entity.Ingredient
import com.shakenbeer.composecocktail.rest.TheCocktailDBService
import com.shakenbeer.composecocktail.rest.model.ApiIngredient
import javax.inject.Inject

class IngredientRemoteRepository @Inject constructor(
    private val restService: TheCocktailDBService,
    private val connectivity: Connectivity
) : IngredientRepository {

    override fun getIngredients(): Result<List<Ingredient>> {
        return when (val result =
            callApi(connectivity, restService.getIngredients())) {
            is Success -> map(result.value.drinks)
            is Error -> result
        }
    }

    private fun map(apiList: List<ApiIngredient>?): Success<List<Ingredient>> {
        return if (apiList != null) {
            Success(
                apiList.asSequence()
                    .map { it.strIngredient1 }
                    .filterNotNull()
                    .map { Ingredient(it) }
                    .toList()
            )
        } else {
            Success(emptyList())
        }
    }
}