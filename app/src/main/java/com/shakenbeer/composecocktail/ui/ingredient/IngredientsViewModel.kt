package com.shakenbeer.composecocktail.ui.ingredient

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shakenbeer.composecocktail.Success
import com.shakenbeer.composecocktail.Error
import com.shakenbeer.composecocktail.usecase.GetIngredientsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class IngredientsViewModel @Inject constructor(
    private val getIngredientsUseCase: GetIngredientsUseCase) : ViewModel() {

    //We need property for Dispatchers.IO to replace it in tests
    //until issue https://github.com/Kotlin/kotlinx.coroutines/issues/982 fixed
    @Suppress("MemberVisibilityCanBePrivate")
    var ioDispatcher = Dispatchers.IO

    private val _ingredients = MutableLiveData<IngredientsViewState>()
    val ingredients: LiveData<IngredientsViewState> by lazy {
        loadIngredients()
        _ingredients
    }

    internal fun loadIngredients() {
        _ingredients.value = LoadingState
        viewModelScope.launch {
            when (val result = withContext(ioDispatcher) { getIngredientsUseCase.execute() }) {
                is Success -> _ingredients.value =
                    if (result.value.isNotEmpty()) {
                        DisplayState(result.value
                            .map { IngredientDisplayItem(it.name, abbr(it.name)) })
                    } else {
                        NoIngredientsState
                    }
                is Error -> _ingredients.value =
                    when (result.reason) {
                        Error.Reason.NO_INTERNET -> NoInternetState
                        else -> {
                            Log.e("IngredientsViewModel", result.throwable.localizedMessage, result.throwable)
                            ErrorState("Server error")
                        }
                    }
            }
        }
    }

    private fun abbr(name: String): String {
        return if (name.isNotEmpty()) {
            name.first().toString()
        } else {
            ""
        }
    }
}