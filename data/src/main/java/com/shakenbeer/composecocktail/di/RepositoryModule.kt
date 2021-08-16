package com.shakenbeer.composecocktail.di

import com.shakenbeer.composecocktail.repository.CategoryRemoteRepository
import com.shakenbeer.composecocktail.repository.CategoryRepository
import com.shakenbeer.composecocktail.repository.IngredientRemoteRepository
import com.shakenbeer.composecocktail.repository.IngredientRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    abstract fun bindCategoryRepository(
        categoryRemoteRepository: CategoryRemoteRepository
    ): CategoryRepository

    @Binds
    abstract fun bindIngredientRepository(
        ingredientRemoteRepository: IngredientRemoteRepository
    ): IngredientRepository
}