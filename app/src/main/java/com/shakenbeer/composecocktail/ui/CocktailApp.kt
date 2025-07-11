package com.shakenbeer.composecocktail.ui

import android.app.Activity
import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.padding
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.ContentAlpha
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.shakenbeer.composecocktail.GetDrinksParam
import com.shakenbeer.composecocktail.R
import com.shakenbeer.composecocktail.ui.category.CategoriesScreen
import com.shakenbeer.composecocktail.ui.drink.DrinksScreen
import com.shakenbeer.composecocktail.ui.drink.DrinksViewModel
import com.shakenbeer.composecocktail.ui.drink.details.DetailedDrinkScreen
import com.shakenbeer.composecocktail.ui.ingredient.IngredientsScreen
import com.shakenbeer.composecocktail.ui.theme.ComposeCocktailTheme
import dagger.hilt.android.EntryPointAccessors
import kotlinx.coroutines.ExperimentalCoroutinesApi

@OptIn(ExperimentalCoroutinesApi::class)
@Composable
fun CocktailApp() {
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
                    DrinksScreen(
                        navController, drinksViewModel(
                            GetDrinksParam(GetDrinksParam.Type.FAVORITE, "")
                        )
                    )
                }
                animatedComposable(
                    route = Screen.Drinks.ByCategory.route
                ) { backStackEntry ->
                    DrinksScreen(
                        navController,
                        drinksViewModel(
                            GetDrinksParam(
                                GetDrinksParam.Type.CATEGORY,
                                backStackEntry.arguments?.getString(name)?.slash() ?: ""
                            )
                        )
                    )
                }
                animatedComposable(
                    route = Screen.Drinks.ByIngredient.route
                ) { backStackEntry ->
                    DrinksScreen(
                        navController,
                        drinksViewModel(
                            GetDrinksParam(
                                GetDrinksParam.Type.INGREDIENT,
                                backStackEntry.arguments?.getString(name)?.slash() ?: ""
                            )
                        )
                    )
                }
                animatedComposable(
                    route = Screen.DetailedDrink.FromCategory.route
                ) { backStackEntry ->
                    DetailedDrinkScreen(
                        drinkId = backStackEntry.arguments?.getString(drinkId) ?: ""
                    )
                }
                animatedComposable(
                    route = Screen.DetailedDrink.FromIngredient.route
                ) { backStackEntry ->
                    DetailedDrinkScreen(
                        drinkId = backStackEntry.arguments?.getString(drinkId) ?: ""
                    )
                }
                composable(Screen.DetailedDrink.FromFavorites.route) { backStackEntry ->
                    DetailedDrinkScreen(
                        drinkId = backStackEntry.arguments?.getString(drinkId) ?: ""
                    )
                }
            }
        }
    }
}


private fun NavGraphBuilder.animatedComposable(
    route: String,
    content: @Composable() (AnimatedContentScope.(NavBackStackEntry) -> Unit)
) {
    composable(
        route = route,
        enterTransition = {
            slideIntoContainer(
                towards = AnimatedContentTransitionScope.SlideDirection.Left,
                animationSpec = tween(700)
            )
        },
        exitTransition = {
            slideOutOfContainer(
                towards = AnimatedContentTransitionScope.SlideDirection.Left,
                animationSpec = tween(700)
            )
        },
        popEnterTransition = {
            slideIntoContainer(
                towards = AnimatedContentTransitionScope.SlideDirection.Right,
                animationSpec = tween(700)
            )
        },
        popExitTransition = {
            slideOutOfContainer(
                towards = AnimatedContentTransitionScope.SlideDirection.Right,
                animationSpec = tween(700)
            )
        },
        content = content
    )
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

@Composable
fun drinksViewModel(getDrinksParam: GetDrinksParam): DrinksViewModel {
    val factory = EntryPointAccessors.fromActivity(
        LocalContext.current as Activity,
        MainActivity.ViewModelFactoryProvider::class.java
    ).drinksViewModelFactory()

    return viewModel(factory = DrinksViewModel.provideFactory(factory, getDrinksParam))
}