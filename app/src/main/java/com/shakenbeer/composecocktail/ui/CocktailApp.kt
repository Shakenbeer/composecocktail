package com.shakenbeer.composecocktail.ui

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.insets.ProvideWindowInsets
import com.shakenbeer.composecocktail.R
import com.shakenbeer.composecocktail.ui.category.Categories
import com.shakenbeer.composecocktail.ui.category.CategoriesScreen
import com.shakenbeer.composecocktail.ui.category.CategoriesViewModel
import com.shakenbeer.composecocktail.ui.ingredient.IngredientsScreen
import com.shakenbeer.composecocktail.ui.theme.ComposeCocktailTheme

@ExperimentalFoundationApi
@Composable
fun CocktailApp() {
    ProvideWindowInsets {
        ComposeCocktailTheme {
            val navController = rememberNavController()
            Scaffold(
                topBar = {
                    TopAppBar(
                        title = { Text(text = stringResource(id = R.string.app_name)) }
                    )
                },
                bottomBar = {
                    CocktailsBottomBar(navController)
                }
            ) { innerPadding ->
                NavHost(
                    navController,
                    startDestination = Screen.Categories.route,
                    Modifier.padding(innerPadding)
                ) {
                    composable(Screen.Categories.route) { CategoriesScreen() }
                    composable(Screen.Ingredients.route) { IngredientsScreen() }
                    composable(Screen.Favorites.route) { Text("Favorites screen") }
                }
            }
        }
    }
}

@Composable
private fun CocktailsBottomBar(navController: NavHostController) {
    BottomNavigation(
        backgroundColor = MaterialTheme.colors.surface,
        contentColor = MaterialTheme.colors.primary
    ) {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentDestination = navBackStackEntry?.destination
        topItems.forEach { screen ->
            BottomNavigationItem(
                selected =currentDestination?.route == screen.route,
                onClick = {
                    navController.navigate(screen.route) {
                        // Pop up to the start destination of the graph to
                        // avoid building up a large stack of destinations
                        // on the back stack as users select items
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        // Avoid multiple copies of the same destination when
                        // reselecting the same item
                        launchSingleTop = true
                        // Restore state when reselecting a previously selected item
                        restoreState = true
                    }
                },
                icon = {
                    Icon(
                        painter = painterResource(screen.iconId),
                        contentDescription = stringResource(screen.labelId)
                    )
                },
                label = {
                    Text(text = stringResource(screen.labelId))
                },
                unselectedContentColor = MaterialTheme.colors.onSurface.copy(alpha = ContentAlpha.medium)
            )
        }
    }
}