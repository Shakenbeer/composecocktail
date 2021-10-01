package com.shakenbeer.composecocktail.ui.drink.details

sealed class DetailedDrinkViewState
object NoInternetState : DetailedDrinkViewState()
object LoadingState : DetailedDrinkViewState()
class ErrorState(val message: String): DetailedDrinkViewState()
object NoDetailsState : DetailedDrinkViewState()
class DisplayState(val drink: DetailedDrinkDisplayItem): DetailedDrinkViewState()