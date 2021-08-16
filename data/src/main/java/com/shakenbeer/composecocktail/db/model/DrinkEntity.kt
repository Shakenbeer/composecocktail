package com.shakenbeer.composecocktail.db.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters

@Entity(tableName = "favorite_drink")
class DrinkEntity(
    @PrimaryKey(autoGenerate = false)
    val id: String,
    val name: String,
    val thumbUrl: String,
    val instruction: String,
    val iba: String,
    val glass: String,
    val alcoholic: Boolean,
    @TypeConverters(IngredientsConverter::class)
    val ingredients: List<String>
)