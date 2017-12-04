package org.firezenk.kartographer

import android.app.Application
import org.firezenk.kartographer.di.ApplicationComponent
import org.firezenk.kartographer.di.ApplicationModule
import org.firezenk.kartographer.di.DaggerApplicationComponent

/**
 * Project: Kartographer
 *
 * Created by Jorge Garrido Oval, aka firezenk on 30/11/17.
 * Copyright © Jorge Garrido Oval 2017
 */
class SampleApplication : Application() {

    companion object {
        lateinit var component: ApplicationComponent
    }

    override fun onCreate() {
        super.onCreate()

        component = DaggerApplicationComponent
                .builder()
                .applicationModule(ApplicationModule(this))
                .build()
    }
}