package com.shakenbeer.composecocktail.db.model

import androidx.room.TypeConverter

private const val SEPARATOR = ";"

class IngredientsConverter {

    @TypeConverter
    fun fromIngredients(ingredients: List<String>): String {
        return ingredients.joinToString(SEPARATOR)
    }

    @TypeConverter
    fun toIngredients(value: String?): List<String> {
        return value?.split(SEPARATOR) ?: emptyList()
    }
}