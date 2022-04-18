package com.mvince.androidcompose.ui.feature.location

import androidx.lifecycle.ViewModel
import com.mvince.androidcompose.repositories.LocationRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LocationViewModel @Inject constructor(
    private val repository: LocationRepository
) : ViewModel() {

    fun getLocations() = repository.getLocations()

}