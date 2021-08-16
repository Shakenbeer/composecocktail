package com.shakenbeer.composecocktail

class GetDrinksParam(val type: Type, val value: String = "") {
    enum class Type { CATEGORY, INGREDIENT, FAVORITE }
}
