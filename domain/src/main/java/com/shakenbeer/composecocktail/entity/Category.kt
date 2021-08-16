package com.shakenbeer.composecocktail.entity

class Category(val name: String, val icon: Icon = Icon.OTHER)

enum class Icon {
    COCKTAIL, BEER, HOT_BEVERAGE, SHOT, HOMEMADE, PARTY, OTHER
}