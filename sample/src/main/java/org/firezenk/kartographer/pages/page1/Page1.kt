package org.firezenk.kartographer.pages.page1

import android.content.Context
import android.view.View
import android.widget.FrameLayout
import kotlinx.android.synthetic.main.page_view.view.*
import org.firezenk.kartographer.R
import org.firezenk.kartographer.SampleApplication
import org.firezenk.kartographer.animations.CrossFade
import org.firezenk.kartographer.annotations.RoutableView
import org.firezenk.kartographer.library.Kartographer
import org.firezenk.kartographer.library.dsl.route
import java.util.*
import javax.inject.Inject

/**
 * Project: Kartographer
 *
 * Created by Jorge Garrido Oval, aka firezenk on 14/12/17.
 * Copyright © Jorge Garrido Oval 2017
 */
@RoutableView(path = "PAGE1", params = [(String::class), (Int::class)])
class Page1(context: Context?) : FrameLayout(context) {

    @Inject lateinit var router: Kartographer

    private var part : String = ""
    private var counter : Int = 0

    companion object {
        fun newInstance(context: Context, uuid: UUID, part: String, counter: Int): Page1 {
            val view = Page1(context)
            view.part = part
            view.counter = counter
            return view
        }
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        View.inflate(context, R.layout.page_view, this)

        SampleApplication.component.injectTo(this)

        part += " -> " + ++counter
        text2.text = context.getString(R.string.route, Page1Route.PATH, part)

        setOnClickListener {
            val route = route<Any> {
                target = Page1Route::class
                anchor = parent
                animation = CrossFade()
            }
            router.next(route, mapOf("part" to part, "counter" to counter))
        }
    }
}