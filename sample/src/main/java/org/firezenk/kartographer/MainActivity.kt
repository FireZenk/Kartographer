package org.firezenk.kartographer

import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import kotlinx.android.synthetic.main.activity_main.*
import org.firezenk.kartographer.annotations.RoutableActivity
import org.firezenk.kartographer.extensions.disableShiftMode
import org.firezenk.kartographer.library.Kartographer
import org.firezenk.kartographer.library.dsl.route
import org.firezenk.kartographer.library.dsl.routeActivity
import org.firezenk.kartographer.library.types.Route
import org.firezenk.kartographer.pages.page1.Page1Route
import org.firezenk.kartographer.pages.page2.Page2Route
import org.firezenk.kartographer.pages.page3.Page3Route
import javax.inject.Inject

/**
 * Project: Kartographer
 *
 * Created by Jorge Garrido Oval, aka firezenk on 20/09/17.
 * Copyright © Jorge Garrido Oval 2017
 */
@RoutableActivity
class MainActivity : AppCompatActivity(), BottomNavigationView.OnNavigationItemSelectedListener {

    @Inject lateinit var router: Kartographer

    private lateinit var page1Route : Route<Any>
    private lateinit var page2Route : Route<Bundle>
    private lateinit var page3Route : Route<Any>

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
            R.id.bottom_action_page1 -> router replayOrNext page1Route
            R.id.bottom_action_page2 -> router.replayOrNext<Bundle>(page2Route)
            R.id.bottom_action_page3 -> router replayOrNext page3Route
        }
        return true
    }

    override fun onBackPressed() {
        router.backOnPath({ super.onBackPressed() })
    }

    private fun defineRoutes() {
        page1Route = route {
            target = Page1Route::class
            params = mapOf("part" to "", "counter" to 0)
            anchor = viewHolder
        }

        page2Route = routeActivity<Bundle> {
            target = Page2Route::class
            params = Bundle().apply { putInt("counter", 10) }
            anchor = viewHolder
        }

        page3Route = route {
            target = Page3Route::class
            params = mapOf("counter" to 100)
            anchor = viewHolder
        }
    }
}
