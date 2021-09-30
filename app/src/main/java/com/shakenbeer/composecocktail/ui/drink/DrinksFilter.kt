package com.shakenbeer.composecocktail.ui.drink

import androidx.annotation.IntDef

const val CATEGORY = 0
const val INGREDIENT = 1
const val FAVORITE = 2

@Retention(AnnotationRetention.SOURCE)
@IntDef(CATEGORY, INGREDIENT, FAVORITE)
annotation class FilterType

class DrinksFilter(@FilterType val type: Int, val filter: String)