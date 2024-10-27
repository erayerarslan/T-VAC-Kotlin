package com.erayerarslan.t_vac_kotlin.di

import com.erayerarslan.t_vac_kotlin.repository.AuthenticationRepository
import com.erayerarslan.t_vac_kotlin.repository.AuthenticationRepositoryImpl
import com.google.firebase.auth.FirebaseAuth
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AuthenticationModule {

    @Provides
    @Singleton
    fun provideFirebaseAuth(): FirebaseAuth = FirebaseAuth.getInstance()

    @Provides
    @Singleton
    fun provideAuthenticationRepository(auth: FirebaseAuth): AuthenticationRepository =
        AuthenticationRepositoryImpl(auth)
}