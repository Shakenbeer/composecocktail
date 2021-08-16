package com.shakenbeer.composecocktail.repository

import androidx.annotation.WorkerThread
import com.shakenbeer.composecocktail.Error
import com.shakenbeer.composecocktail.Result
import com.shakenbeer.composecocktail.Success
import com.shakenbeer.composecocktail.connectivity.Connectivity
import com.shakenbeer.composecocktail.entity.Category
import com.shakenbeer.composecocktail.entity.Icon
import com.shakenbeer.composecocktail.rest.TheCocktailDBService
import com.shakenbeer.composecocktail.rest.model.ApiCategory
import javax.inject.Inject

class CategoryRemoteRepository @Inject constructor(
    private val restService: TheCocktailDBService,
    private val connectivity: Connectivity
) : CategoryRepository {

    @WorkerThread
    override fun getCategories(): Result<List<Category>> {
        return when (val result =
            callApi(connectivity, restService.getCategories())) {
            is Success -> map(result.value.drinks)
            is Error -> result
        }
    }

    private fun map(apiList: List<ApiCategory>?): Success<List<Category>> {
        return if (apiList != null) {
            Success(
                apiList.asSequence()
                    .map { it.strCategory }
                    .filterNotNull()
                    .map { Category(it, iconFromName(it)) }
                    .toList()
            )
        } else {
            Success(emptyList())
        }
    }

    private fun iconFromName(name: String): Icon {
        return when (name) {
            "Cocktail" -> Icon.COCKTAIL
            "Shot" -> Icon.SHOT
            "Coffee / Tea" -> Icon.HOT_BEVERAGE
            "Beer" -> Icon.BEER
            "Homemade Liqueur" -> Icon.HOMEMADE
            "Punch / Party Drink" -> Icon.PARTY
            else -> Icon.OTHER
        }
    }

}