package com.shakenbeer.composecocktail.ui.category

sealed class CategoriesViewState
object NoInternetState : CategoriesViewState()
object LoadingState : CategoriesViewState()
class ErrorState(val message: String): CategoriesViewState()
object NoCategoriesState : CategoriesViewState()
class DisplayState(val categories: List<CategoryDisplayItem>): CategoriesViewState()