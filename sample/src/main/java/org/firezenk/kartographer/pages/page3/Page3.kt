package org.firezenk.kartographer.pages.page3

import android.content.Context
import android.view.View
import android.widget.FrameLayout
import kotlinx.android.synthetic.main.page_view.view.*
import org.firezenk.kartographer.R
import org.firezenk.kartographer.SampleApplication
import org.firezenk.kartographer.animations.PushLeft
import org.firezenk.kartographer.annotations.RoutableView
import org.firezenk.kartographer.library.Kartographer
import org.firezenk.kartographer.library.dsl.route
import org.firezenk.kartographer.pages.Page3Route
import java.util.*
import javax.inject.Inject

/**
 * Project: Kartographer
 *
 * Created by Jorge Garrido Oval, aka firezenk on 14/12/17.
 * Copyright © Jorge Garrido Oval 2017
 */
@RoutableView(path = "PAGE3", params = [(Int::class)])
class Page3(context: Context?) : FrameLayout(context) {

    @Inject lateinit var router: Kartographer

    private var counter : Int = 0

    companion object {
        fun newInstance(context: Context, uuid: UUID, counter: Int): Page3 {
            val view = Page3(context)
            view.counter = counter
            return view
        }
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        View.inflate(context, R.layout.page_view, this)

        SampleApplication.component.injectTo(this)

        text2.text = "${Page3Route.PATH}: $counter"

        setOnClickListener {
            val route = route<Any> {
                target = Page3Route::class
                anchor = parent
                animation = PushLeft()
            }
            router.next<Any>(route, arrayOf(counter+100))
        }
    }
}