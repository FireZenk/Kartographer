package org.firezenk.kartographer.di

import dagger.Module
import dagger.Provides
import org.firezenk.kartographer.SampleApplication
import org.firezenk.kartographer.library.Kartographer
import javax.inject.Singleton

/**
 * Project: Kartographer
 *
 * Created by Jorge Garrido Oval, aka firezenk on 30/11/17.
 * Copyright © Jorge Garrido Oval 2017
 */
@Module
class ApplicationModule(private val application: SampleApplication) {

    @Provides
    @Singleton
    fun getApp() = application

    @Provides
    @Singleton
    fun provideKartographer() = Kartographer
}