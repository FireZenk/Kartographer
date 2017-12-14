package org.firezenk.kartographer.di

import dagger.Component
import org.firezenk.kartographer.MainActivity
import org.firezenk.kartographer.pages.Page1
import org.firezenk.kartographer.pages.Page2
import org.firezenk.kartographer.pages.Page3
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

    fun injectTo(screen: Page1)

    fun injectTo(screen: Page2)

    fun injectTo(screen: Page3)

    fun injectTo(screen: MainActivity)
}