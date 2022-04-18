package com.mvince.androidcompose.repositories

import com.mvince.androidcompose.utils.LocationManager
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LocationRepository @Inject constructor(private val sharedLocationManager: LocationManager) {
    fun getLocations() = sharedLocationManager.locationFlow()
}