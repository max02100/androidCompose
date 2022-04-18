package com.mvince.androidcompose

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.GlobalScope

@HiltAndroidApp
class FoodiesApp : Application() {
    val applicationScope = GlobalScope
}