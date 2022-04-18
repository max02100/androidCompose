package com.mvince.androidcompose.ui.feature.location

import android.Manifest
import android.location.Location
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.mvince.androidcompose.R

@ExperimentalPermissionsApi
@Composable
fun LocationScreen(viewModel: LocationViewModel) {

    val lifecycleOwner = LocalLifecycleOwner.current
    val exampleEntitiesFlowLifecycleAware = remember(viewModel.getLocations(), lifecycleOwner) {
        viewModel.getLocations()
            .flowWithLifecycle(lifecycleOwner.lifecycle, Lifecycle.State.STARTED)
    }


    // Location permission state
    val locationPermissionState = rememberMultiplePermissionsState(
        listOf(
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION
        )
    )

    Scaffold(
        topBar = {
            TopAppBar(
                navigationIcon = {
                    Icon(
                        imageVector = Icons.Default.Home,
                        modifier = Modifier.padding(horizontal = 12.dp),
                        contentDescription = "Action icon"
                    )
                },
                title = { Text(stringResource(R.string.app_name)) },
                backgroundColor = MaterialTheme.colors.background
            )
        },
    ) {
        if (locationPermissionState.allPermissionsGranted) {
            val userLocation: Location? by exampleEntitiesFlowLifecycleAware.collectAsState(initial = null)
            Text(text = userLocation?.toString() ?: "Pas de position")
        } else {
            locationPermissionState.launchMultiplePermissionRequest()
        }

    }
}