package com.mvince.androidcompose.repositories

import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.mvince.androidcompose.model.Post
import com.mvince.androidcompose.model.State
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PostRepository @Inject constructor() {
    private val mPostsCollection = Firebase.firestore.collection("POSTS")

    fun getPostData(): Flow<State<Post>> = callbackFlow {
        trySend(State.Loading())
        val postDocument = mPostsCollection.document("post1")

        val subscription = postDocument.addSnapshotListener { snapshot, exception ->
            exception?.let {
                trySend(State.Failed(it.message.toString()))
                cancel(it.message.toString())
            }
            if (snapshot!!.exists()) {
                trySend(State.Success(snapshot.toObject(Post::class.java)!!)).isSuccess
            } else {
                trySend(State.Failed("Not found")).isFailure
            }
        }
        awaitClose { subscription.remove() }
    }

    fun getAllPosts(): Flow<State<List<Post>>> = callbackFlow {
        trySend(State.Loading())
        val postDocument = mPostsCollection

        val subscription = postDocument.addSnapshotListener { snapshot, exception ->
            exception?.let {
                trySend(State.Failed(it.message.toString()))
                cancel(it.message.toString())
            }

            snapshot?.let {
                val list: List<Post> = it.documents.map { it.toObject(Post::class.java)!! }
                trySend(State.Success(list)).isSuccess
            } ?: trySend(State.Failed("Not found")).isFailure
        }
        awaitClose { subscription.remove() }
    }

    fun addPost(post: Post): Flow<State<Post>> = callbackFlow {
        trySend(State.Loading())
        val postDocument = mPostsCollection.document("post_id_0").set(post, SetOptions.merge())
        //val postDocument = mPostsCollection.add(post)

        postDocument.addOnCompleteListener {
            if (it.isSuccessful) {
                trySend(State.Success(post)).isSuccess
            } else if (it.isComplete && !it.isSuccessful) {
                trySend(State.Failed(it.exception?.message.toString()))
                cancel(it.exception?.message.toString())
            }
        }

        awaitClose { postDocument.isComplete }
    }
}