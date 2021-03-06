package org.firezenk.kartographer.di

import dagger.Component
import org.firezenk.kartographer.MainActivity
import org.firezenk.kartographer.pages.page1.Page1
import org.firezenk.kartographer.pages.page2.Page2
import org.firezenk.kartographer.pages.page2.subpage.Subpage
import org.firezenk.kartographer.pages.page3.Page3
import org.firezenk.kartographer.pages.page4.Page4Activity
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

    fun injectTo(screen: Subpage)

    fun injectTo(screen: Page2)

    fun injectTo(screen: Page3)

    fun injectTo(screen: MainActivity)

    fun injectTo(screen: Page4Activity)
}