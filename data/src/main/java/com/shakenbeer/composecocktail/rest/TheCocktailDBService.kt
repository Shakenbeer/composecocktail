package com.shakenbeer.composecocktail.rest

import com.shakenbeer.composecocktail.rest.model.ApiCategories
import com.shakenbeer.composecocktail.rest.model.ApiDetailedDrinks
import com.shakenbeer.composecocktail.rest.model.ApiDrinks
import com.shakenbeer.composecocktail.rest.model.ApiIngredients
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface TheCocktailDBService {

    @GET("list.php")
    fun getCategories(@Query("c") c: String = "list"): Call<ApiCategories>

    @GET("list.php")
    fun getIngredients(@Query("i") i: String = "list"): Call<ApiIngredients>

    @GET("filter.php")
    fun getDrinksByCategory(@Query("c") categoryName: String): Call<ApiDrinks>

    @GET("filter.php")
    fun getDrinksByIngredient(@Query("i") ingredientName: String): Call<ApiDrinks>

    @GET("lookup.php")
    fun getDrinkById(@Query("i") id: String): Call<ApiDetailedDrinks>

}