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
    val details = listOf(iba, if (alcoholic) "Alcoholic" else "Non alcoholic", glass)
        .filter { it.isNotBlank() }.joinToString("\n")

    val displayIngredients = ingredients.joinToString("\n")

    companion object {
        val NO_DETAILS = DetailedDrink("", "", "", "", "", "", true, emptyList(), false)
    }
}
