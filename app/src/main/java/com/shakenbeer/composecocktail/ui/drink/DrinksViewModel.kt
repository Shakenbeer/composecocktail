package com.shakenbeer.composecocktail.ui.drink

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.navigation.NavController
import com.shakenbeer.composecocktail.Error
import com.shakenbeer.composecocktail.GetDrinksParam
import com.shakenbeer.composecocktail.Success
import com.shakenbeer.composecocktail.ui.Screen
import com.shakenbeer.composecocktail.ui.favorites
import com.shakenbeer.composecocktail.usecase.GetDrinksByCategoryUseCase
import com.shakenbeer.composecocktail.usecase.GetDrinksByIngredientUseCase
import com.shakenbeer.composecocktail.usecase.GetFavoritesDrinksUseCase
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map

class DrinksViewModel @AssistedInject constructor(
    @Assisted private val getDrinksParam: GetDrinksParam,
    getDrinksByCategoryUseCase: GetDrinksByCategoryUseCase,
    getDrinksByIngredientUseCase: GetDrinksByIngredientUseCase,
    getFavoritesDrinksUseCase: GetFavoritesDrinksUseCase
) : ViewModel() {

    private val refreshTrigger = MutableSharedFlow<Unit>(replay = 1).also { it.tryEmit(Unit) }

    @ExperimentalCoroutinesApi
    val drinks: LiveData<DrinksViewState> = refreshTrigger.flatMapLatest {
        when (getDrinksParam.type) {
            GetDrinksParam.Type.CATEGORY -> getDrinksByCategoryUseCase(getDrinksParam.value)
            GetDrinksParam.Type.INGREDIENT -> getDrinksByIngredientUseCase(getDrinksParam.value)
            GetDrinksParam.Type.FAVORITE -> getFavoritesDrinksUseCase()
        }
    }.map { result ->
        when (result) {
            is Success -> if (result.value.isNotEmpty()) {
                DisplayState(result.value
                    .map { DrinkDisplayItem(it.id, it.name, it.thumbUrl) })
            } else {
                NoDrinksState
            }
            is Error -> when (result.reason) {
                Error.Reason.NO_INTERNET -> NoInternetState
                else -> {
                    Log.e(
                        "DrinksViewModel",
                        result.throwable.localizedMessage,
                        result.throwable
                    )
                    ErrorState("Server error")
                }
            }
        }
    }.asLiveData()

    internal fun loadDrinks() {
        refreshTrigger.tryEmit(Unit)
    }

    internal fun navigateTo(navController: NavController, drinkId: String) {
        when (getDrinksParam.type) {
            GetDrinksParam.Type.CATEGORY -> navController.navigate(
                Screen.DetailedDrink.FromCategory.route(
                    getDrinksParam.value,
                    drinkId
                )
            )
            GetDrinksParam.Type.INGREDIENT -> navController.navigate(
                Screen.DetailedDrink.FromIngredient.route(
                    getDrinksParam.value,
                    drinkId
                )
            )
            GetDrinksParam.Type.FAVORITE -> navController.navigate(
                Screen.DetailedDrink.FromFavorites.route(
                    favorites,
                    drinkId
                )
            )
        }
    }

    @AssistedFactory
    interface Factory {
        fun create(getDrinksParam: GetDrinksParam): DrinksViewModel
    }

    @Suppress("UNCHECKED_CAST")
    companion object {
        fun provideFactory(
            assistedFactory: Factory,
            getDrinksParam: GetDrinksParam
        ): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return assistedFactory.create(getDrinksParam) as T
            }
        }
    }
}