package com.shakenbeer.composecocktail.ui.ingredient

sealed class IngredientsViewState
object NoInternetState : IngredientsViewState()
object LoadingState : IngredientsViewState()
class ErrorState(val message: String): IngredientsViewState()
object NoIngredientsState : IngredientsViewState()
class DisplayState(val ingredients: List<IngredientDisplayItem>): IngredientsViewState()