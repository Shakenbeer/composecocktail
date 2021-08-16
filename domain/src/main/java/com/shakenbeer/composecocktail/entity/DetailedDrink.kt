package com.shakenbeer.composecocktail.entity

data class DetailedDrink(
    val id: String,
    val name: String,
    val thumbUrl: String,
    val instruction: String,
    val iba: String,
    val glass: String,
    val alcoholic: Boolean,
    val ingredients: List<String>,
    val isFavorite: Boolean
) {

    companion object {
        val NO_DETAILS = DetailedDrink("", "", "", "", "", "", true, emptyList(), false)
    }
}
