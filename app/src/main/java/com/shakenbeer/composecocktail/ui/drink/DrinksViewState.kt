package com.shakenbeer.composecocktail.ui.drink

sealed class DrinksViewState
object NoInternetState : DrinksViewState()
object LoadingState : DrinksViewState()
class ErrorState(val message: String): DrinksViewState()
object NoDrinksState : DrinksViewState()
class DisplayState(val drinks: List<DrinkDisplayItem>): DrinksViewState()