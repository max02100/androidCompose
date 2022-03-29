package com.mvince.androidcompose.ui.feature.categories

import com.mvince.androidcompose.model.FoodItem


class FoodCategoriesContract {

    data class State(
        val categories: List<FoodItem> = listOf(),
        val isLoading: Boolean = false
    )

    sealed class Effect {
        object DataWasLoaded : Effect()
    }
}