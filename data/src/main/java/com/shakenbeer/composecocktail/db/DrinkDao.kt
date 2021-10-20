package com.shakenbeer.composecocktail.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.shakenbeer.composecocktail.db.model.DrinkEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface DrinkDao {

    @Query("select * from favorite_drink")
    fun getFavorites(): Flow<List<DrinkEntity>>

    @Query("select * from favorite_drink")
    fun getFavoritesSync(): List<DrinkEntity>

    @Query("select * from favorite_drink where :id = id")
    fun findFavorite(id: String): DrinkEntity?

    @Insert
    fun addFavorite(drink: DrinkEntity)

    @Delete
    fun removeFavorite(drink: DrinkEntity)
}