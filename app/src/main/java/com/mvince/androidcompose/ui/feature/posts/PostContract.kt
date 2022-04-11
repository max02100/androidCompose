package com.mvince.androidcompose.ui.feature.posts

import com.mvince.androidcompose.model.Post

class PostContract {
    data class State(
        val posts: List<Post> = listOf(),
        val isLoading: Boolean = false
    )
}