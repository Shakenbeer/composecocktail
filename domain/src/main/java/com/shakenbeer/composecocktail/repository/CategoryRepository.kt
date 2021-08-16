package com.shakenbeer.composecocktail.repository

import com.shakenbeer.composecocktail.Result
import com.shakenbeer.composecocktail.entity.Category

interface CategoryRepository {
    fun getCategories(): Result<List<Category>>
}