package com.shakenbeer.composecocktail.ui.drink

import android.util.Log
import androidx.lifecycle.*
import androidx.navigation.NavController
import com.shakenbeer.composecocktail.GetDrinksParam
import com.shakenbeer.composecocktail.Success
import com.shakenbeer.composecocktail.Error
import com.shakenbeer.composecocktail.ui.Screen
import com.shakenbeer.composecocktail.ui.favorites
import com.shakenbeer.composecocktail.usecase.GetDrinksUseCase
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DrinksViewModel @AssistedInject constructor(
    @Assisted private val getDrinksParam: GetDrinksParam,
    private val getDrinksUseCase: GetDrinksUseCase)
    : ViewModel() {

    //We need property for Dispatchers.IO to replace it in tests
    //until issue https://github.com/Kotlin/kotlinx.coroutines/issues/982 fixed
    var ioDispatcher = Dispatchers.IO

    private val _drinks = MutableLiveData<DrinksViewState>()
    val drinks: LiveData<DrinksViewState> by lazy {
        loadDrinks()
        _drinks
    }

    internal fun loadDrinks() {
        _drinks.value = LoadingState
        viewModelScope.launch {
            when (val result =
                withContext(ioDispatcher) { getDrinksUseCase.execute(getDrinksParam) }) {
                is Success -> _drinks.value =
                    if (result.value.isNotEmpty()) {
                        DisplayState(result.value
                            .map { DrinkDisplayItem(it.id, it.name, it.thumbUrl) })
                    } else {
                        NoDrinksState
                    }
                is Error -> _drinks.value =
                    when (result.reason) {
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
        }
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
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                return assistedFactory.create(getDrinksParam) as T
            }
        }
    }
}