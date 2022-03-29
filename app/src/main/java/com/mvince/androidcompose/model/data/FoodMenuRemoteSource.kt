package com.mvince.androidcompose.model.data

import com.mvince.androidcompose.model.FoodItem
import com.mvince.androidcompose.model.response.FoodCategoriesResponse
import com.mvince.androidcompose.model.response.MealsResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FoodMenuRemoteSource @Inject constructor(private val foodMenuApi: FoodMenuApi) {

    private var cachedCategories: List<FoodItem>? = null

    suspend fun getFoodCategories(): List<FoodItem> = withContext(Dispatchers.IO) {
        var cachedCategories = cachedCategories
        if (cachedCategories == null) {
            cachedCategories = foodMenuApi.getFoodCategories().mapCategoriesToItems()
            this@FoodMenuRemoteSource.cachedCategories = cachedCategories
        }
        return@withContext cachedCategories
    }

    suspend fun getMealsByCategory(categoryId: String) = withContext(Dispatchers.IO) {
        val categoryName = getFoodCategories().first { it.id == categoryId }.name
        return@withContext foodMenuApi.getMealsByCategory(categoryName).mapMealsToItems()
    }

    private fun FoodCategoriesResponse.mapCategoriesToItems(): List<FoodItem> {
        return this.categories.map { category ->
            FoodItem(
                id = category.id,
                name = category.name,
                description = category.description,
                thumbnailUrl = category.thumbnailUrl
            )
        }
    }

    private fun MealsResponse.mapMealsToItems(): List<FoodItem> {
        return this.meals.map { category ->
            FoodItem(
                id = category.id,
                name = category.name,
                thumbnailUrl = category.thumbnailUrl
            )
        }
    }

}