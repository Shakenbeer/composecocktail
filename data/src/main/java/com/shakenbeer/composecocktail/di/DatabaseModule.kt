package com.shakenbeer.composecocktail.di

import android.content.Context
import androidx.room.Room
import com.shakenbeer.composecocktail.db.CocktailDatabase
import com.shakenbeer.composecocktail.db.DrinkDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Singleton
    @Provides
    fun provideDatabase(@ApplicationContext context: Context): CocktailDatabase {
        return Room.databaseBuilder(
            context,
            CocktailDatabase::class.java, "cocktails"
        ).build()
    }

    @Singleton
    @Provides
    fun provideAlbumDao(db: CocktailDatabase): DrinkDao = db.drinkDao()
}