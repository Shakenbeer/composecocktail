package com.shakenbeer.composecocktail.ui.drink

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shakenbeer.composecocktail.GetDrinksParam
import com.shakenbeer.composecocktail.Success
import com.shakenbeer.composecocktail.Error
import com.shakenbeer.composecocktail.usecase.GetDrinksUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class DrinksViewModel @Inject constructor(private val getDrinksUseCase: GetDrinksUseCase)
    : ViewModel() {

    //We need property for Dispatchers.IO to replace it in tests
    //until issue https://github.com/Kotlin/kotlinx.coroutines/issues/982 fixed
    var ioDispatcher = Dispatchers.IO

    private val _drinks = MutableLiveData<DrinksViewState>()
    val drinks: LiveData<DrinksViewState> by lazy {
        loadDrinks()
        _drinks
    }

    private lateinit var getDrinksParam: GetDrinksParam

    internal fun onResume() {
        if (::getDrinksParam.isInitialized && getDrinksParam.type == GetDrinksParam.Type.FAVORITE) {
            loadDrinks()
        }
    }

    internal fun onModeDefined(drinksFilter: DrinksFilter) {
        getDrinksParam = when (drinksFilter.type) {
            CATEGORY -> GetDrinksParam(GetDrinksParam.Type.CATEGORY, drinksFilter.filter)
            INGREDIENT -> GetDrinksParam(GetDrinksParam.Type.INGREDIENT, drinksFilter.filter)
            else -> GetDrinksParam(GetDrinksParam.Type.FAVORITE)
        }
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
}