package com.mvince.androidcompose.ui.feature.posts

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mvince.androidcompose.model.Post
import com.mvince.androidcompose.model.State
import com.mvince.androidcompose.repositories.PostRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PostViewModel @Inject constructor(
    private val repository: PostRepository
) : ViewModel() {

    init {
        getPost()
    }

    private val mState = MutableStateFlow<State<Post>>(State.loading())
    val state: StateFlow<State<Post>>
        get() = mState

    private fun getPost() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                repository.getPostData().collect {
                    mState.value = it
                }
            } catch (ex: Exception) {
                mState.value = State.failed(ex.localizedMessage)
            }
        }
    }

    fun addPost(post: Post) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.addPost(post).collect {
                mState.value = it
            }
        }
    }
}