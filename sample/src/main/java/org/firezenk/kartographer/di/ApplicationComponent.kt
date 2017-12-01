package org.firezenk.kartographer.di

import android.widget.FrameLayout
import dagger.Component
import org.firezenk.kartographer.MainActivity
import javax.inject.Singleton

/**
 * Project: Kartographer
 *
 * Created by Jorge Garrido Oval, aka firezenk on 30/11/17.
 * Copyright © Jorge Garrido Oval 2017
 */
@Singleton
@Component(modules = arrayOf(ApplicationModule::class))
interface ApplicationComponent {

    fun injectTo(screen: FrameLayout)

    fun injectTo(screen: MainActivity)
}