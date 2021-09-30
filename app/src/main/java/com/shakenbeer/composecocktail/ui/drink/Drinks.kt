package com.shakenbeer.composecocktail.ui.drink

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.GridCells
import androidx.compose.foundation.lazy.LazyVerticalGrid
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.shakenbeer.composecocktail.R
import com.shakenbeer.composecocktail.ui.common.Loading
import com.shakenbeer.composecocktail.ui.common.Trouble
import androidx.compose.material.Text
import androidx.compose.ui.Alignment
import coil.compose.rememberImagePainter
import com.shakenbeer.composecocktail.ui.theme.transparentIndigoDark

@ExperimentalFoundationApi
@Composable
fun DrinksScreen(drinksFilter: DrinksFilter, drinksViewModel: DrinksViewModel = hiltViewModel()) {

    drinksViewModel.onModeDefined(drinksFilter)

    val state: DrinksViewState by drinksViewModel.drinks.observeAsState(LoadingState)

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
            is NoDrinksState -> Trouble(
                painter = painterResource(id = R.drawable.ic_cup_off_24dp),
                message = stringResource(R.string.no_drinks_found)
            )
            is DisplayState -> {
                Drinks(it.drinks)
            }
        }
    }
}

@ExperimentalFoundationApi
@Composable
fun Drinks(drinks: List<DrinkDisplayItem>) {
    LazyVerticalGrid(
        cells = GridCells.Fixed(2),
        contentPadding = PaddingValues(4.dp, 8.dp)
    ) {
        drinks.forEach { drink ->
            item {
                Drink(drink)
            }
        }
    }
}

@Composable
fun Drink(drink: DrinkDisplayItem) {
    Box(modifier = Modifier
        .fillMaxWidth()
        .aspectRatio(1.0f)
        .padding(4.dp)) {
        Image(
            painter = rememberImagePainter(
                data = drink.thumbUrl,
                builder = {
                    crossfade(true)
                    placeholder(R.drawable.cocktail_placeholder)
                }),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
        )
        Text(
            text = drink.name,
            modifier = Modifier
                .background(transparentIndigoDark)
                .padding(8.dp)
                .fillMaxWidth()
                .wrapContentHeight()
                .align(Alignment.BottomStart),
            color = MaterialTheme.colors.onPrimary,
            style = MaterialTheme.typography.body2
        )
    }
}