package org.firezenk.kartographer

import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import kotlinx.android.synthetic.main.activity_main.*
import org.firezenk.kartographer.extensions.disableShiftMode
import org.firezenk.kartographer.library.Kartographer
import org.firezenk.kartographer.library.Path
import org.firezenk.kartographer.library.Route
import org.firezenk.kartographer.library.dsl.route
import org.firezenk.kartographer.pages.Page1Route
import org.firezenk.kartographer.pages.Page3Route
import org.firezenk.kartographer.pages.page2.Page2Route
import javax.inject.Inject

/**
 * Project: Kartographer
 *
 * Created by Jorge Garrido Oval, aka firezenk on 20/09/17.
 * Copyright © Jorge Garrido Oval 2017
 */
class MainActivity : AppCompatActivity(), BottomNavigationView.OnNavigationItemSelectedListener {

    @Inject lateinit var router: Kartographer

    private lateinit var page1Route : Route<Any>
    private lateinit var page2Route : Route<Any>
    private lateinit var page3Route : Route<Any>

    private var currentPath : String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        SampleApplication.component.injectTo(this)

        with(navigation) {
            disableShiftMode()
            setOnNavigationItemSelectedListener(this@MainActivity)
        }

        defineRoutes()

        with(router) { last(viewHolder) or next(page1Route) }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.bottom_action_page1 -> {
                currentPath = Page1Route.PATH
                router replayOrNext page1Route
            }
            R.id.bottom_action_page2 -> {
                currentPath = Page2Route.PATH
                router replayOrNext page2Route
            }
            R.id.bottom_action_page3 -> {
                currentPath = Page3Route.PATH
                router replayOrNext page3Route
            }
        }
        return true
    }

    override fun onBackPressed() {
        if (currentPath == null) {
            router back {super.onBackPressed()}
        } else {
            router back(Path(currentPath!!))
        }
    }

    private fun defineRoutes() {
        page1Route = route<Any> {
            target = Page1Route::class
            params = arrayOf("", 0)
            anchor = viewHolder
        }

        page2Route = route<Any> {
            target = Page2Route::class
            params = arrayOf(10)
            anchor = viewHolder
        }

        page3Route = route<Any> {
            target = Page3Route::class
            params = arrayOf(100)
            anchor = viewHolder
        }
    }
}
