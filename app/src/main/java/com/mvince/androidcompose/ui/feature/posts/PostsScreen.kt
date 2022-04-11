package com.mvince.androidcompose.ui.feature.posts

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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.mvince.androidcompose.R
import com.mvince.androidcompose.model.Post
import com.mvince.androidcompose.model.State
import com.mvince.androidcompose.ui.feature.categories.LoadingBar


@Composable
fun PostsScreen(viewModel: PostViewModel) {
    val scaffoldState = rememberScaffoldState()
    val showDialog = remember { mutableStateOf(false) }

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
        when (val state = viewModel.state.collectAsState().value) {
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
    }
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
            addFunction.invoke(Post(
                postAuthor = author,
                postContent = content
            ))
        }) {

        }
    }
}

@Composable
fun PresentDialog(showDialog: MutableState<Boolean>) {
    AlertDialog(
        onDismissRequest = {
            showDialog.value = false
        },
        title = {
            Text(text = "Alert Dialog")
        },
        text = {
            Text("JetPack Compose Alert Dialog!")
        },
        confirmButton = {
            Button(onClick = {
                showDialog.value = false
            }) {
                Text(text = "toto")
            }
        }
    )
}