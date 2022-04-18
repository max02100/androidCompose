package com.mvince.androidcompose.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.mvince.androidcompose.ui.NavigationKeys.Arg.FOOD_CATEGORY_ID
import com.mvince.androidcompose.ui.feature.categories.FoodCategoriesScreen
import com.mvince.androidcompose.ui.feature.categories.FoodCategoriesViewModel
import com.mvince.androidcompose.ui.feature.category_details.FoodCategoryDetailsScreen
import com.mvince.androidcompose.ui.feature.category_details.FoodCategoryDetailsViewModel
import com.mvince.androidcompose.ui.feature.location.LocationScreen
import com.mvince.androidcompose.ui.feature.location.LocationViewModel
import com.mvince.androidcompose.ui.feature.posts.PostViewModel
import com.mvince.androidcompose.ui.feature.posts.PostsScreen
import com.mvince.androidcompose.ui.theme.ComposeSampleTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.receiveAsFlow

// Single Activity per app
@ExperimentalPermissionsApi
@AndroidEntryPoint
class EntryPointActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ComposeSampleTheme {
                FoodApp()
            }
        }
    }
}

@ExperimentalPermissionsApi
@Preview
@Composable
private fun FoodApp() {
    val navController = rememberNavController()
    NavHost(navController, startDestination = NavigationKeys.Route.LOCATION) {
        composable(route = NavigationKeys.Route.FOOD_CATEGORIES_LIST) {
            FoodCategoriesDestination(navController)
        }
        composable(
            route = NavigationKeys.Route.FOOD_CATEGORY_DETAILS,
            arguments = listOf(
                navArgument(NavigationKeys.Arg.FOOD_CATEGORY_ID) {
                    type = NavType.StringType
                }
            )
        ) {
            FoodCategoryDetailsDestination()
        }
        composable(route = NavigationKeys.Route.POSTS) {
            PostsDestination()
        }
        composable(route = NavigationKeys.Route.LOCATION) {
            LocationDestination()
        }
    }
}

@Composable
private fun FoodCategoriesDestination(navController: NavHostController) {
    val viewModel: FoodCategoriesViewModel = hiltViewModel()
    FoodCategoriesScreen(
        state = viewModel.state,
        effectFlow = viewModel.effects.receiveAsFlow(),
        onNavigationRequested = { itemId ->
            navController.navigate("${NavigationKeys.Route.FOOD_CATEGORIES_LIST}/$itemId")
        }
    )
}

@ExperimentalPermissionsApi
@Composable
private fun PostsDestination() {
    val viewModel: PostViewModel = hiltViewModel()
    PostsScreen(viewModel = viewModel)
}

@ExperimentalPermissionsApi
@Composable
private fun LocationDestination() {
    val viewModel: LocationViewModel = hiltViewModel()
    LocationScreen(viewModel = viewModel)
}

@Composable
private fun FoodCategoryDetailsDestination() {
    val viewModel: FoodCategoryDetailsViewModel = hiltViewModel()
    FoodCategoryDetailsScreen(viewModel.state)
}

object NavigationKeys {

    object Arg {
        const val FOOD_CATEGORY_ID = "foodCategoryName"
    }

    object Route {
        const val FOOD_CATEGORIES_LIST = "food_categories_list"
        const val POSTS = "posts"
        const val LOCATION = "location"
        const val FOOD_CATEGORY_DETAILS = "$FOOD_CATEGORIES_LIST/{$FOOD_CATEGORY_ID}"
    }
}
