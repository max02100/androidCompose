package com.mvince.androidcompose.ui.feature.category_details

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel

import androidx.lifecycle.viewModelScope
import com.mvince.androidcompose.model.data.FoodMenuRemoteSource
import com.mvince.androidcompose.ui.NavigationKeys
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class FoodCategoryDetailsViewModel @Inject constructor(
    private val stateHandle: SavedStateHandle,
    private val repository: FoodMenuRemoteSource
) : ViewModel() {

    var state by mutableStateOf(
        FoodCategoryDetailsContract.State(
            null, listOf(
            )
        )
    )
        private set

    init {
        viewModelScope.launch {
            val categoryId = stateHandle.get<String>(NavigationKeys.Arg.FOOD_CATEGORY_ID)
                ?: throw IllegalStateException("No categoryId was passed to destination.")
            val categories = repository.getFoodCategories()
            val category = categories.first { it.id == categoryId }
            state = state.copy(category = category)
            val foodItems = repository.getMealsByCategory(categoryId)
            state = state.copy(categoryFoodItems = foodItems)
        }
    }

}
