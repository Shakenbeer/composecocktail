package com.shakenbeer.composecocktail.ui.drink.details

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shakenbeer.composecocktail.Success
import com.shakenbeer.composecocktail.Error
import com.shakenbeer.composecocktail.entity.DetailedDrink
import com.shakenbeer.composecocktail.usecase.GetDetailedDrinkUseCase
import com.shakenbeer.composecocktail.usecase.MakeFavoriteDrinkUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class DetailedDrinkViewModel @Inject constructor(
    private val getDetailedDrinkUseCase: GetDetailedDrinkUseCase,
    private val makeFavoriteDrinkUseCase: MakeFavoriteDrinkUseCase
) : ViewModel() {

    //We need property for Dispatchers.IO to replace it in tests
    //until issue https://github.com/Kotlin/kotlinx.coroutines/issues/982 fixed
    var ioDispatcher = Dispatchers.IO

    private val _drink = MutableLiveData<DetailedDrinkViewState>()
    val drink: LiveData<DetailedDrinkViewState> by lazy {
        loadDrink()
        _drink
    }

    private lateinit var drinkId: String

    internal fun onDrinkId(drinkId: String) {
        this.drinkId = drinkId
    }

    internal fun loadDrink() {
        _drink.value = LoadingState
        viewModelScope.launch {
            when (val result =
                withContext(ioDispatcher) { getDetailedDrinkUseCase.execute(drinkId) }) {
                is Success ->
                    if (result.value != DetailedDrink.NO_DETAILS) {
                        _drink.value = DisplayState(result.value)
                    } else {
                        _drink.value = NoDetailsState
                    }
                is Error -> _drink.value =
                    when (result.reason) {
                        Error.Reason.NO_INTERNET -> NoInternetState
                        else -> {
                            Log.e(
                                "DetailedDrinkViewModel",
                                result.throwable.localizedMessage,
                                result.throwable
                            )
                            ErrorState("Server error")
                        }
                    }
            }
        }
    }

    fun onFavoriteClick() {
        _drink.value?.let { state ->
            if (state is DisplayState) {
                viewModelScope.launch {
                    val drinkResult = withContext(ioDispatcher) {
                        makeFavoriteDrinkUseCase.execute(state.drink)
                    }
                    if (drinkResult is Success) {
                        _drink.value = DisplayState(drinkResult.value)
                    }
                }
            }
        }
    }
}