package com.mvince.androidcompose.ui.feature.posts

import android.graphics.Bitmap
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.launch
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionStatus
import com.google.accompanist.permissions.rememberPermissionState
import com.mvince.androidcompose.R
import com.mvince.androidcompose.model.Post


@ExperimentalPermissionsApi
@Composable
fun PostsScreen(viewModel: PostViewModel) {
    val scaffoldState = rememberScaffoldState()

    Scaffold(
        scaffoldState = scaffoldState,
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
        /*when (val state = viewModel.state.collectAsState().value) {
            is State.Loading ->
                LoadingBar()
            is State.Failed -> {
                LaunchedEffect(scaffoldState.snackbarHostState) {
                    scaffoldState.snackbarHostState.showSnackbar(
                        "Rien à afficher",
                        duration = SnackbarDuration.Long
                    )
                }
            }
            is State.Success -> Column {
                Text(text = state.data.toString())
                PostCreator {
                    viewModel.addPost(it)
                }
            }
        }
    }*/
        FeatureThatRequiresCameraPermission()
    }

    @Composable
    fun PostCreator(addFunction: (Post) -> Unit) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth()
        ) {
            var content by remember { mutableStateOf("") }
            var author by remember { mutableStateOf("") }
            TextField(
                value = author,
                onValueChange = { author = it },
                label = { Text(text = "Saisissez votre Auteur") })
            TextField(
                value = content,
                onValueChange = { content = it },
                label = { Text(text = "Saisissez votre Contenu") })
            Button(onClick = {
                addFunction.invoke(
                    Post(
                        postAuthor = author,
                        postContent = content
                    )
                )
            }) {

            }
        }
    }
}

@ExperimentalPermissionsApi
@Composable
private fun FeatureThatRequiresCameraPermission() {
    val bitmapFromCamera = remember { mutableStateOf<Bitmap?>(null) }
    val launcher =
        rememberLauncherForActivityResult(ActivityResultContracts.TakePicturePreview()) {
            bitmapFromCamera.value = it
        }
    // Camera permission state
    val cameraPermissionState = rememberPermissionState(android.Manifest.permission.CAMERA)

    when (cameraPermissionState.status) {
        // If the camera permission is granted, then show screen with the feature enabled
        PermissionStatus.Granted -> {
            if(bitmapFromCamera.value == null) {
                Button(onClick = { launcher.launch() }) {
                    Text("Take a picture")
                }
            } else {
                bitmapFromCamera.let {
                    val data = it.value
                    if (data != null) {
                        Image(
                            bitmap = data.asImageBitmap(),
                            contentDescription = null,
                            modifier = Modifier.fillMaxSize()
                        )
                    }
                }
            }
        }
        is PermissionStatus.Denied -> {
            Column {
                val status = cameraPermissionState.status as PermissionStatus.Denied
                val textToShow = if (status.shouldShowRationale) {
                    "The camera is important for this app. Please grant the permission."
                } else {
                    "Camera permission required for this feature to be available. " +
                            "Please grant the permission"
                }
                Text(textToShow)
                Button(onClick = { cameraPermissionState.launchPermissionRequest() }) {
                    Text("Request permission")
                }
            }
        }
    }
}