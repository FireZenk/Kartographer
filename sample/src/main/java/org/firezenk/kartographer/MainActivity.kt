package org.firezenk.kartographer

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import org.firezenk.kartographer.library.Kartographer
import org.firezenk.kartographer.library.Route
import org.firezenk.kartographer.tabs.TabsViewRoute

/**
 * Project: Kartographer
 *
 * Created by Jorge Garrido Oval, aka firezenk on 20/09/17.
 * Copyright © Jorge Garrido Oval 2017
 */
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        with(Kartographer) {
            debug()
            val route = Route<Any>(TabsViewRoute::class.java, arrayOf<Any>(), placeholder)
            last(this@MainActivity, placeholder) or next(this@MainActivity, route)
        }
    }

    override fun onBackPressed() {
        if (!Kartographer.back(this))
            super.onBackPressed()
    }
}
