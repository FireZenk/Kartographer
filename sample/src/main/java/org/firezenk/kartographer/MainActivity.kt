package org.firezenk.kartographer

import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import kotlinx.android.synthetic.main.activity_main.*
import org.firezenk.kartographer.extensions.disableShiftMode
import org.firezenk.kartographer.library.Kartographer
import org.firezenk.kartographer.library.Path
import org.firezenk.kartographer.library.dsl.route
import org.firezenk.kartographer.pages.Page1Route
import org.firezenk.kartographer.pages.Page2Route
import org.firezenk.kartographer.pages.Page3Route
import javax.inject.Inject

/**
 * Project: Kartographer
 *
 * Created by Jorge Garrido Oval, aka firezenk on 20/09/17.
 * Copyright © Jorge Garrido Oval 2017
 */
class MainActivity : AppCompatActivity(), BottomNavigationView.OnNavigationItemSelectedListener {

    @Inject lateinit var router: Kartographer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        SampleApplication.component.injectTo(this)

        navigation.let {
            it.disableShiftMode()
            it.setOnNavigationItemSelectedListener(this)
        }

        with(router) {
            last(viewHolder) or next(route<Any> {
                target = Page1Route::class
                params = arrayOf("", 0)
                anchor = viewHolder
            })
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.bottom_action_page1 -> {
                with(router) {
                    replay(Path(Page1Route.PATH)) or next(route<Any> {
                        target = Page1Route::class
                        params = arrayOf("", 0)
                        anchor = viewHolder
                    })
                }
            }
            R.id.bottom_action_page2 -> {
                with(router) {
                    replay(Path(Page2Route.PATH)) or next(route<Any> {
                        target = Page2Route::class
                        params = arrayOf("", 0)
                        anchor = viewHolder
                    })
                }
            }
            R.id.bottom_action_page3 -> {
                with(router) {
                    replay(Path(Page3Route.PATH)) or next(route<Any> {
                        target = Page3Route::class
                        params = arrayOf("", 0)
                        anchor = viewHolder
                    })
                }
            }
        }
        return true
    }

    override fun onBackPressed() {
        router back {
            super.onBackPressed()
        }
    }
}
