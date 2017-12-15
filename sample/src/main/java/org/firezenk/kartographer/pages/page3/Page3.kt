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
import javax.inject.Inject

/**
 * Project: Kartographer
 *
 * Created by Jorge Garrido Oval, aka firezenk on 14/12/17.
 * Copyright © Jorge Garrido Oval 2017
 */
@RoutableView(path = "PAGE3")
class Page3(context: Context?) : FrameLayout(context) {

    @Inject lateinit var router: Kartographer

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        View.inflate(context, R.layout.page_view, this)

        SampleApplication.component.injectTo(this)

        val counter: Int? = router.payload<Int>("counter")

        text2.text = "${Page3Route.PATH}: $counter"

        setOnClickListener {
            router next route<Any> {
                target = Page3Route::class
                params = mapOf("counter" to counter!! + 100)
                anchor = parent
                animation = PushLeft(100)
            }
        }
    }
}