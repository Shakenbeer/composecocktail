package com.shakenbeer.composecocktail.ui.ingredient

import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.LocalContentColor
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawStyle
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextAlign.Companion.Center
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.shakenbeer.composecocktail.R
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.shakenbeer.composecocktail.ui.Screen
import com.shakenbeer.composecocktail.ui.common.Loading
import com.shakenbeer.composecocktail.ui.common.Trouble
import com.shakenbeer.composecocktail.ui.theme.ComposeCocktailTheme

@ExperimentalFoundationApi
@Composable
fun IngredientsScreen(
    navController: NavController,
    ingredientsViewModel: IngredientsViewModel = hiltViewModel()) {

    Log.d("CCK", "IngredientsScreen: ")

    val state: IngredientsViewState
            by ingredientsViewModel.ingredients.observeAsState(LoadingState)

    state.let {
        when (it) {
            is LoadingState -> Loading()
            is NoInternetState -> Trouble(
                painter = painterResource(id = R.drawable.ic_wifi_off_24dp),
                message = stringResource(R.string.no_internet_connection)
            )
            is ErrorState -> Trouble(
                painter = painterResource(id = R.drawable.ic_alert_circle_24dp),
                message = it.message
            )
            is NoIngredientsState -> Trouble(
                painter = painterResource(id = R.drawable.ic_cup_off_24dp),
                message = stringResource(R.string.no_ingredients_found)
            )
            is DisplayState -> {
                Ingredients(
                    { ingredient -> navController.navigate(Screen.Drinks.ByIngredient.route(ingredient)) },
                    it.ingredients
                )
            }
        }
    }
}

@Composable
fun Ingredients(onNavigate: (String) -> Unit, ingredients: List<IngredientDisplayItem>) {
    LazyColumn {
        ingredients.forEach { ingredient ->
            item {
                Ingredient(onNavigate, ingredient)
            }
        }
    }
}

@Composable
fun Ingredient(onNavigate: (String) -> Unit, item: IngredientDisplayItem) {
    Row(
        modifier = Modifier
            .clickable { onNavigate(item.name) }
            .fillMaxWidth()
            .height(56.dp)
            .padding(16.dp, 0.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        val color = LocalContentColor.current
        Box(modifier = Modifier
            .drawBehind {
                drawCircle(color = color, style = Stroke(width = 4.dp.toPx()))
            }
            .size(40.dp)
        ) {
            Text(
                modifier = Modifier.align(Alignment.Center),
                fontFamily = FontFamily.SansSerif,
                fontWeight = FontWeight.Black,
                fontSize = 24.sp,
                text = item.abbr)
        }


        Text(
            modifier = Modifier.padding(16.dp, 0.dp),
            text = item.name
        )
    }
}

@Preview
@Composable
fun IngredientPreview() {
    ComposeCocktailTheme {
        Ingredient({}, item = IngredientDisplayItem("Applejack", "A"))
    }
}