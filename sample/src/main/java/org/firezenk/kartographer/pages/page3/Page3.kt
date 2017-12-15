package org.firezenk.kartographer.pages.page3

import android.annotation.SuppressLint
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

    companion object {
        fun newInstance(context: Context, uuid: UUID, counter: Int) = Page3(context)
    }

    @SuppressLint("SetTextI18n")
    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        View.inflate(context, R.layout.page_view, this)

        SampleApplication.component.injectTo(this)

        val counter = router.payload<Array<Any>>()!![0] as Int

        text2.text = "${Page3Route.PATH}: $counter"

        setOnClickListener {
            router next route<Any> {
                target = Page3Route::class
                params = arrayOf(counter + 100)
                anchor = parent
                animation = PushLeft(100)
            }
        }
    }
}