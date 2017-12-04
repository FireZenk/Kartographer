package org.firezenk.kartographer

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import org.firezenk.kartographer.library.Kartographer
import org.firezenk.kartographer.library.dsl.route
import org.firezenk.kartographer.tabs.TabsViewRoute
import javax.inject.Inject

/**
 * Project: Kartographer
 *
 * Created by Jorge Garrido Oval, aka firezenk on 20/09/17.
 * Copyright © Jorge Garrido Oval 2017
 */
class MainActivity : AppCompatActivity() {

    @Inject lateinit var router: Kartographer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        SampleApplication.component.injectTo(this)

        with(router) {
            debug()
            last(placeholder) or next(route<Any> {
                target = TabsViewRoute::class
                params = arrayOf<Any>()
                anchor = placeholder
            })
        }
    }

    override fun onBackPressed() {
        if (!router.back())
            super.onBackPressed()
    }
}
