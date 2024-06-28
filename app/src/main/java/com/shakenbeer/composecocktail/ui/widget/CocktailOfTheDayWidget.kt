package com.shakenbeer.composecocktail.ui.widget

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.glance.GlanceId
import androidx.glance.GlanceTheme
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.SizeMode
import androidx.glance.appwidget.provideContent
import coil.ImageLoader
import coil.request.ErrorResult
import coil.request.ImageRequest
import com.shakenbeer.composecocktail.R
import com.shakenbeer.composecocktail.entity.DetailedDrink
import com.shakenbeer.composecocktail.ui.widget.AspectRatio.Companion.asDouble
import com.shakenbeer.composecocktail.ui.widget.ImageUtils.getMaxWidgetMemoryAllowedSizeInBytes
import com.shakenbeer.composecocktail.usecase.GetRandomDrinkUseCase
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.android.EntryPointAccessors
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CocktailOfTheDayWidget : GlanceAppWidget() {

    @EntryPoint
    @InstallIn(SingletonComponent::class)
    interface RandomDrinkProviderEntryPoint {
        fun getRandomDrinkUseCase(): GetRandomDrinkUseCase
    }

    // Unlike the "Single" size mode, using "Exact" allows us to have better control over rendering in
    // different sizes. And, unlike the "Responsive" mode, it doesn't cause several views for each
    // supported size to be held in the widget host's memory.
    override val sizeMode: SizeMode = SizeMode.Exact

    override suspend fun provideGlance(context: Context, id: GlanceId) {

        val appContext = context.applicationContext ?: throw IllegalStateException()
        val randomDrinkProviderEntryPoint = EntryPointAccessors
            .fromApplication(appContext, RandomDrinkProviderEntryPoint::class.java)
        val getRandomDrinkUseCase = randomDrinkProviderEntryPoint.getRandomDrinkUseCase()

        Log.d("COCK_DEBUG", "provideGlance: 1")
        val initialDrink = getRandomDrinkUseCase.execute()
        val initialBitmap =
            withContext(Dispatchers.IO) { fetchImage(context, initialDrink.thumbUrl) }

        provideContent {
            var cocktail by remember { mutableStateOf(Cocktail(initialDrink, initialBitmap)) }
            val drink = cocktail.drink
            val bitmap = cocktail.bitmap
            val coroutineScope = rememberCoroutineScope()

            GlanceTheme {
                TextWithImageLayout(
                    title = drink.name,
                    titleIconRes = R.drawable.ic_glass_cocktail_24dp,
                    titleBarActionIconRes = R.drawable.sample_refresh_icon,
                    titleBarActionIconContentDescription = "Refresh",
                    titleBarAction = {
                        coroutineScope.launch {
                            Log.d("COCK_DEBUG", "provideGlance: 2")
                            val d = getRandomDrinkUseCase.execute()
                            val b = withContext(Dispatchers.IO) { fetchImage(context, d.thumbUrl) }
                            cocktail = Cocktail(d, b)
                        }
                    },
                    data = TextWithImageData(
                        textData = TextData(
                            key = drink.id,
                            primary = drink.name,
                            secondary = drink.instruction,
                            caption = drink.displayIngredients
                        ),
                        imageData = ImageData(bitmap)
                    )
                )
            }
        }
    }

    private suspend fun fetchImage(context: Context, url: String): Bitmap? {
        val maxAllowedBytes = context.getMaxWidgetMemoryAllowedSizeInBytes()
        val imageSizeLimit = ImageUtils.getMaxPossibleImageSize(
            aspectRatio = AspectRatio.Ratio16x9.asDouble(),
            memoryLimitBytes = maxAllowedBytes,
            maxImages = 1
        )
        val maxWidth = imageSizeLimit.width
        val maxHeight = imageSizeLimit.height

        var bitmap: Bitmap? = null

        val result = ImageLoader(context).execute(
            ImageRequest.Builder(context)
                .data(url)
                .size(maxWidth, maxHeight)
                .target { res: Drawable ->
                    bitmap = (res as BitmapDrawable).bitmap
                }.build()
        )

        if (result is ErrorResult) {
            Log.e("CocktailWidget", "Failed to load the image:", result.throwable)
        }

        return bitmap
    }
}

class Cocktail(
    val drink: DetailedDrink,
    val bitmap: Bitmap?
)