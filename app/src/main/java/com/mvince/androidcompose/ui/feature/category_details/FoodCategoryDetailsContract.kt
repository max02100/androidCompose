package com.mvince.androidcompose.ui.feature.category_details


import com.mvince.androidcompose.model.FoodItem


class FoodCategoryDetailsContract {
    data class State(
        val category: FoodItem?,
        val categoryFoodItems: List<FoodItem>
    )
}