package com.shakenbeer.composecocktail.ui

import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.insets.ProvideWindowInsets
import com.shakenbeer.composecocktail.R
import com.shakenbeer.composecocktail.ui.category.CategoriesScreen
import com.shakenbeer.composecocktail.ui.drink.*
import com.shakenbeer.composecocktail.ui.drink.details.DetailedDrinkScreen
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
                    startDestination = Screen.Tab.Categories.route,
                    Modifier.padding(innerPadding)
                ) {
                    composable(Screen.Tab.Categories.route) { CategoriesScreen(navController) }
                    composable(Screen.Tab.Ingredients.route) { IngredientsScreen(navController) }
                    composable(Screen.Tab.Favorites.route) {
                        DrinksScreen(navController, DrinksFilter(FAVORITE, ""))
                    }
                    composable(Screen.Drinks.ByCategory.route) { backStackEntry ->
                        DrinksScreen(
                            navController,
                            DrinksFilter(
                                CATEGORY,
                                backStackEntry.arguments?.getString(name)?.slash() ?: ""
                            )
                        )
                    }
                    composable(Screen.Drinks.ByIngredient.route) { backStackEntry ->
                        DrinksScreen(
                            navController,
                            DrinksFilter(
                                INGREDIENT,
                                backStackEntry.arguments?.getString(name)?.slash() ?: ""
                            )
                        )
                    }
                    composable(Screen.DetailedDrink.FromCategory.route) { backStackEntry ->
                        DetailedDrinkScreen(
                            drinkId = backStackEntry.arguments?.getString(drinkId) ?: ""
                        )
                    }
                    composable(Screen.DetailedDrink.FromIngredient.route) { backStackEntry ->
                        DetailedDrinkScreen(
                            drinkId = backStackEntry.arguments?.getString(drinkId) ?: ""
                        )
                    }
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
        currentDestination?.hierarchy?.forEachIndexed { i, dest ->
            Log.d("CCK", "$i " + (dest.route ?: "NULL"))
        }
        topItems.forEach { screen ->
            BottomNavigationItem(
                selected = currentDestination?.route?.startsWith(screen.route) == true,
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