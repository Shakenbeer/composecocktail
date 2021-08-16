package com.shakenbeer.composecocktail.usecase

import com.shakenbeer.composecocktail.Result
import com.shakenbeer.composecocktail.entity.Category
import com.shakenbeer.composecocktail.repository.CategoryRepository
import javax.inject.Inject

class GetCategoriesUseCase @Inject constructor(private val categoryRepository: CategoryRepository) {
    fun execute(): Result<List<Category>> {
        return categoryRepository.getCategories()
    }
}