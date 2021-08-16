package com.shakenbeer.composecocktail.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.shakenbeer.composecocktail.db.model.DrinkEntity
import com.shakenbeer.composecocktail.db.model.IngredientsConverter

@Database(entities = [DrinkEntity::class], version = 1)
@TypeConverters(IngredientsConverter::class)
abstract class CocktailDatabase : RoomDatabase() {
    abstract fun drinkDao(): DrinkDao
}