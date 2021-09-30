package com.shakenbeer.composecocktail.di

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import com.google.gson.GsonBuilder
import com.shakenbeer.composecocktail.BuildConfig
import com.shakenbeer.composecocktail.Const
import com.shakenbeer.composecocktail.connectivity.Connectivity
import com.shakenbeer.composecocktail.rest.TheCocktailDBService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideRestService(): TheCocktailDBService {
        return provideRetrofit().create(TheCocktailDBService::class.java)
    }

    private fun provideRetrofit(): Retrofit {
        val gson = GsonBuilder()
            .create()

        val client = OkHttpClient.Builder().apply {
            if (BuildConfig.DEBUG) {
                addInterceptor(HttpLoggingInterceptor().also {
                    it.level = HttpLoggingInterceptor.Level.BODY
                })
            }
        }.build()

        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create(gson))
            .baseUrl(Const.COCKTAIL_API_URL)
            .client(client)
            .build()
    }

    //TODO rid of deprecation using {@link android.net.ConnectivityManager.NetworkCallback} API
    @Provides
    @Singleton
    fun provideConnectivity(@ApplicationContext context: Context): Connectivity {
        return object : Connectivity {
            override fun isConnectedToInternet(): Boolean {
                val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
                val activeNetwork: NetworkInfo? = cm.activeNetworkInfo
                return activeNetwork?.isConnectedOrConnecting ?: false
            }
        }
    }
}