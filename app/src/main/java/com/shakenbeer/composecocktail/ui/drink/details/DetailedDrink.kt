package com.shakenbeer.composecocktail.ui.drink.details

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberImagePainter
import com.shakenbeer.composecocktail.R
import com.shakenbeer.composecocktail.entity.DetailedDrink
import com.shakenbeer.composecocktail.ui.common.Loading
import com.shakenbeer.composecocktail.ui.common.Trouble
import com.shakenbeer.composecocktail.ui.theme.transparentIndigoDark

@Composable
fun DetailedDrinkScreen(
    drinkId: String,
    detailedDrinkViewModel: DetailedDrinkViewModel = hiltViewModel()
) {
    detailedDrinkViewModel.onDrinkId(drinkId)

    val state: DetailedDrinkViewState by detailedDrinkViewModel.drink.observeAsState(LoadingState)

    state.let {
        when (it) {
            is LoadingState -> Loading()
            is NoInternetState -> Trouble(
                icon = R.drawable.ic_wifi_off_24dp,
                message = stringResource(R.string.no_internet_connection)
            ) { detailedDrinkViewModel.loadDrink() }
            is ErrorState -> Trouble(
                icon = R.drawable.ic_alert_circle_24dp,
                message = it.message
            ) { detailedDrinkViewModel.loadDrink() }
            is NoDetailsState -> Trouble(
                icon = R.drawable.ic_cup_off_24dp,
                message = stringResource(R.string.no_drink_details_found)
            ) { detailedDrinkViewModel.loadDrink() }
            is DisplayState -> DetailedDrink(it.drink) {
                detailedDrinkViewModel.onFavoriteClick()
            }
        }
    }
}

@Composable
fun DetailedDrink(drink: DetailedDrink, favoritesClick: () -> Unit) {
    Column(Modifier.verticalScroll(rememberScrollState())) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp, 16.dp, 16.dp, 0.dp)
                .aspectRatio(1.0f)
        ) {
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
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(transparentIndigoDark)
                    .align(Alignment.BottomCenter)
            ) {
                Text(
                    text = drink.name,
                    modifier = Modifier
                        .padding(8.dp)
                        .fillMaxWidth()
                        .wrapContentHeight()
                        .align(Alignment.CenterStart),
                    fontSize = 20.sp,
                    color = MaterialTheme.colors.onPrimary,
                    style = MaterialTheme.typography.body2
                )
                IconButton(
                    onClick = { favoritesClick() },
                    modifier = Modifier
                        .width(48.dp)
                        .align(Alignment.CenterEnd)
                ) {
                    Icon(
                        modifier = Modifier,
                        painter = painterResource(
                            id = if (drink.isFavorite) R.drawable.ic_favorite_24dp
                            else R.drawable.ic_heart_outline_24dp
                        ),
                        contentDescription = stringResource(id = R.string.content_description_favorite_icon),
                        tint = MaterialTheme.colors.onPrimary
                    )
                }
            }
        }
        Row(
            Modifier
                .fillMaxWidth()
                .padding(16.dp, 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = drink.details,
                modifier = Modifier
                    .fillMaxWidth(0.5f)
                    .padding(0.dp, 0.dp, 8.dp, 0.dp),
                style = MaterialTheme.typography.body1
            )
            Text(
                text = drink.displayIngredients,
                style = MaterialTheme.typography.body1
            )
        }
        Text(
            text = drink.instruction,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp, 8.dp, 16.dp, 8.dp),
            style = MaterialTheme.typography.body2
        )
    }
}