package org.firezenk.kartographer.tabs.right

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import kotlinx.android.synthetic.main.righttab_view.view.*
import org.firezenk.kartographer.R
import org.firezenk.kartographer.SampleApplication
import org.firezenk.kartographer.animations.PushLeft
import org.firezenk.kartographer.annotations.RoutableView
import org.firezenk.kartographer.library.Kartographer
import org.firezenk.kartographer.library.dsl.route
import java.util.*
import javax.inject.Inject

/**
 * Project: Kartographer
 *
 * Created by Jorge Garrido Oval, aka firezenk on 26/09/17.
 * Copyright © Jorge Garrido Oval 2017
 */
@RoutableView(path = "RIGHT", params = arrayOf(Int::class))
class RightView(context: Context?) : FrameLayout(context) {

    @Inject lateinit var router: Kartographer

    companion object {

        var counter : Int = 0

        fun newInstance(context: Context, uuid: UUID, counter: Int): RightView {
            this.counter = counter
            return RightView(context)
        }
    }

    init {
        View.inflate(getContext(), R.layout.righttab_view, this)

        SampleApplication.component.injectTo(this)

        text2.text = "${RightViewRoute.PATH}: $counter"

        setOnClickListener {
            router next route<Any> {
                target = RightViewRoute::class
                params = arrayOf(++counter)
                anchor = parent as ViewGroup
                animation = PushLeft()
            }
        }
    }
}