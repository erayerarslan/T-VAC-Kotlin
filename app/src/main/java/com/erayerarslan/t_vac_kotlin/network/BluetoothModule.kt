package com.erayerarslan.t_vac_kotlin.network

import android.app.Application
import android.bluetooth.BluetoothAdapter
import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideBluetoothAdapter(): BluetoothAdapter {
        return BluetoothAdapter.getDefaultAdapter() ?: throw IllegalStateException("Bluetooth not supported")
    }

    @Provides
    @Singleton
    fun provideContext(@ApplicationContext context: Context): Context {
        return context
    }
}



