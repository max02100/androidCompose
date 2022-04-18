package com.mvince.androidcompose.di

import android.content.Context
import com.mvince.androidcompose.FoodiesApp
import com.mvince.androidcompose.utils.LocationManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.DelicateCoroutinesApi
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataModule {

    @OptIn(DelicateCoroutinesApi::class)
    @Provides
    @Singleton
    fun provideSharedLocationManager(
        @ApplicationContext context: Context
    ): LocationManager =
        LocationManager(context, (context.applicationContext as FoodiesApp).applicationScope)
}