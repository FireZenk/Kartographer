package org.firezenk.kartographer.di

import dagger.Module
import dagger.Provides
import org.firezenk.kartographer.SampleApplication
import org.firezenk.kartographer.animations.ContextMonitor
import org.firezenk.kartographer.library.Kartographer
import timber.log.Timber
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
    fun provideApplication() = application

    @Provides
    @Singleton
    fun provideKartographer(application: SampleApplication): Kartographer {
        val monitor = ContextMonitor()
        application.registerActivityLifecycleCallbacks(monitor)
        return Kartographer(application, monitor).debug({Timber.d(it)})
    }
}