package org.firezenk.kartographer.di

import dagger.Component
import org.firezenk.kartographer.MainActivity
import org.firezenk.kartographer.tabs.TabsView
import org.firezenk.kartographer.tabs.left.LeftView
import org.firezenk.kartographer.tabs.right.RightView
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

    fun injectTo(screen: TabsView)

    fun injectTo(screen: LeftView)

    fun injectTo(screen: RightView)

    fun injectTo(screen: MainActivity)
}