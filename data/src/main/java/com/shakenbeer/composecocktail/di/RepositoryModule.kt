package com.shakenbeer.composecocktail.di

import com.shakenbeer.composecocktail.repository.*
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

    @Binds
    abstract fun bindDrinkRepository(
        drinkComposeRepository: DrinkComposeRepository
    ): DrinkRepository

    @Binds
    abstract fun bindDrinkDetailsRepository(
        detailedDrinkComposeRepository: DetailedDrinkComposeRepository
    ): DetailedDrinkRepository
}