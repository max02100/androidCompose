package com.mvince.androidcompose.utils

import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import android.os.Looper
import android.util.Log
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.shareIn
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LocationManager @Inject constructor(
    private val context: Context,
    externalScope: CoroutineScope
) {
    // Set up the Fused Location Provider and LocationRequest
    private val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context);
    private val locationRequest = LocationRequest.create().apply {
        interval = 100
        fastestInterval = 50
        priority = LocationRequest.PRIORITY_HIGH_ACCURACY
    }

    @ExperimentalCoroutinesApi
    @SuppressLint("MissingPermission")
    private val _locationUpdates = callbackFlow<Location> {
        val callback = object : LocationCallback() {
            override fun onLocationResult(result: LocationResult) {
                Log.d(
                    this@LocationManager.javaClass.name,
                    "New location: ${result.lastLocation}"
                )
                // Send the new location to the Flow observers
                trySend(result.lastLocation)
            }
        }
        Log.d(this@LocationManager.javaClass.name, "Starting location updates")

        fusedLocationClient.requestLocationUpdates(
            locationRequest,
            callback,
            Looper.getMainLooper()
        ).addOnFailureListener { e ->
            close(e) // in case of exception, close the Flow
        }

        awaitClose {
            Log.d(this@LocationManager.javaClass.name, "Stopping location updates")
            fusedLocationClient.removeLocationUpdates(callback) // clean up when Flow collection ends
        }
    }.shareIn(
        externalScope,
        replay = 0,
        started = SharingStarted.WhileSubscribed()
    )

    @ExperimentalCoroutinesApi
    fun locationFlow(): Flow<Location> {
        return _locationUpdates
    }
}