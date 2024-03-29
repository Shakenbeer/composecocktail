package com.shakenbeer.composecocktail.ui.ingredient

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.LocalContentColor
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.shakenbeer.composecocktail.R
import com.shakenbeer.composecocktail.ui.Screen
import com.shakenbeer.composecocktail.ui.common.Loading
import com.shakenbeer.composecocktail.ui.common.Trouble
import com.shakenbeer.composecocktail.ui.theme.ComposeCocktailTheme

@Composable
fun IngredientsScreen(
    navController: NavController,
    ingredientsViewModel: IngredientsViewModel = hiltViewModel()
) {

    val state: IngredientsViewState
            by ingredientsViewModel.ingredients.observeAsState(LoadingState)

    state.let {
        when (it) {
            is LoadingState -> Loading()
            is NoInternetState -> Trouble(
                icon = R.drawable.ic_wifi_off_24dp,
                message = stringResource(R.string.no_internet_connection)
            ) { ingredientsViewModel.loadIngredients() }
            is ErrorState -> Trouble(
                icon = R.drawable.ic_alert_circle_24dp,
                message = it.message
            ) { ingredientsViewModel.loadIngredients() }
            is NoIngredientsState -> Trouble(
                icon = R.drawable.ic_cup_off_24dp,
                message = stringResource(R.string.no_ingredients_found)
            ) { ingredientsViewModel.loadIngredients() }
            is DisplayState -> {
                Ingredients(
                    { ingredient ->
                        navController.navigate(
                            Screen.Drinks.ByIngredient.route(
                                ingredient
                            )
                        )
                    },
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
                text = item.abbr
            )
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