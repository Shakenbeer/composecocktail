package com.shakenbeer.composecocktail.ui.category

import android.util.Log
import androidx.annotation.DrawableRes
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shakenbeer.composecocktail.Error
import com.shakenbeer.composecocktail.R
import com.shakenbeer.composecocktail.Success
import com.shakenbeer.composecocktail.entity.Icon
import com.shakenbeer.composecocktail.usecase.GetCategoriesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject


@HiltViewModel
class CategoriesViewModel @Inject constructor(
    private val getCategoriesUseCase: GetCategoriesUseCase) : ViewModel() {

    //We need property for Dispatchers.IO to replace it in tests
    //until issue https://github.com/Kotlin/kotlinx.coroutines/issues/982 fixed
    @Suppress("MemberVisibilityCanBePrivate")
    var ioDispatcher = Dispatchers.IO

    private val _categories = MutableLiveData<CategoriesViewState>()
    val categories: LiveData<CategoriesViewState> by lazy {
        loadCategories()
        _categories
    }

    internal fun loadCategories() {
        _categories.value = LoadingState
        viewModelScope.launch {
            when (val result = withContext(ioDispatcher) { getCategoriesUseCase.execute() }) {
                is Success -> _categories.value =
                    if (result.value.isNotEmpty()) {
                        DisplayState(result.value
                            .map { CategoryDisplayItem(it.name, iconToDrawableRes(it.icon)) })
                    } else {
                        NoCategoriesState
                    }
                is Error -> _categories.value =
                    when (result.reason) {
                        Error.Reason.NO_INTERNET -> NoInternetState
                        else -> {
                            Log.e("CategoriesViewModel", result.throwable.localizedMessage, result.throwable)
                            ErrorState("Server error")
                        }
                    }
            }
        }
    }

    @DrawableRes
    private fun iconToDrawableRes(icon: Icon): Int {
        return when (icon) {
            Icon.COCKTAIL -> R.drawable.ic_glass_cocktail_24dp
            Icon.SHOT -> R.drawable.ic_fire_24dp
            Icon.BEER -> R.drawable.ic_beer_24dp
            Icon.HOT_BEVERAGE -> R.drawable.ic_coffee_24dp
            Icon.HOMEMADE -> R.drawable.ic_tonic_skull_24dp
            Icon.PARTY -> R.drawable.ic_party_popper_24dp
            Icon.OTHER -> R.drawable.ic_drink_24dp
        }
    }
}