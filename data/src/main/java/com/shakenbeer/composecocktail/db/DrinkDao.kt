package com.shakenbeer.composecocktail.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.shakenbeer.composecocktail.db.model.DrinkEntity

@Dao
interface DrinkDao {

    @Query("select * from favorite_drink")
    fun getFavorites(): List<DrinkEntity>

    @Query("select * from favorite_drink where :id = id")
    fun findFavorite(id: String): DrinkEntity?

    @Insert
    fun addFavorite(drink: DrinkEntity)

    @Delete
    fun removeFavorite(drink: DrinkEntity)
}