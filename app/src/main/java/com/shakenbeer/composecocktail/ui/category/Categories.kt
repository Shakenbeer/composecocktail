package com.shakenbeer.composecocktail.ui.category

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.shakenbeer.composecocktail.R
import com.shakenbeer.composecocktail.ui.common.Loading
import com.shakenbeer.composecocktail.ui.common.Trouble
import com.shakenbeer.composecocktail.ui.theme.ComposeCocktailTheme

@ExperimentalFoundationApi
@Composable
fun CategoriesScreen(categoriesViewModel: CategoriesViewModel = hiltViewModel()) {

    val state: CategoriesViewState
            by categoriesViewModel.categories.observeAsState(LoadingState)

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
            is NoCategoriesState -> Trouble(
                painter = painterResource(id = R.drawable.ic_cup_off_24dp),
                message = stringResource(R.string.no_categories_found)
            )
            is DisplayState -> {
                Categories(it.categories)
            }
        }
    }
}

@ExperimentalFoundationApi
@Composable
fun Categories(categories: List<CategoryDisplayItem>) {
    LazyColumn {
        categories.forEach { category ->
            item {
                Category(category)
            }
        }
    }
}

@Composable
fun Category(item: CategoryDisplayItem) {
    Row(
        modifier = Modifier
            .clickable { }
            .fillMaxWidth()
            .height(56.dp)
            .padding(16.dp, 0.dp),
        verticalAlignment = CenterVertically
    ) {
        Icon(
            modifier = Modifier.size(40.dp),
            painter = painterResource(id = item.icon),
            contentDescription = stringResource(id = R.string.content_description_category_icon)
        )
        Text(
            modifier = Modifier.padding(16.dp, 0.dp),
            text = item.name
        )
    }
}

@Preview
@Composable
fun CategoryItemPreview() {
    ComposeCocktailTheme {
        Category(CategoryDisplayItem("Cocktail", R.drawable.ic_glass_cocktail_24dp))
    }
}


