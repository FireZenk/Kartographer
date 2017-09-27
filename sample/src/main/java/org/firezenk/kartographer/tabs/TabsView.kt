package org.firezenk.kartographer.tabs

import android.content.Context
import android.view.View
import android.widget.FrameLayout
import kotlinx.android.synthetic.main.tabbed_view.view.*
import org.firezenk.kartographer.R
import org.firezenk.kartographer.annotations.RoutableView
import org.firezenk.kartographer.library.Kartographer
import org.firezenk.kartographer.library.Path
import org.firezenk.kartographer.library.Route
import org.firezenk.kartographer.tabs.left.LeftViewRoute
import org.firezenk.kartographer.tabs.right.RightViewRoute
import java.util.*

/**
 * Project: Kartographer
 *
 * Created by Jorge Garrido Oval, aka firezenk on 21/09/17.
 * Copyright © Jorge Garrido Oval 2017
 */
@RoutableView()
class TabsView(context: Context?) : FrameLayout(context) {

    companion object {

        fun newInstance(context: Context, uuid: UUID): TabsView {
            return TabsView(context)
        }
    }

    init {
        View.inflate(getContext(), R.layout.tabbed_view, this)
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()

        with(Kartographer) {
            next(context, Route<Any>(LeftViewRoute::class.java, arrayOf(100), leftPlaceholder))
            next(context, Route<Any>(RightViewRoute::class.java, arrayOf(200), rightPlaceholder))
        }

        backLeft.setOnClickListener { Kartographer.back(context, Path(LeftViewRoute.PATH)) }
        backRight.setOnClickListener { Kartographer.back(context, Path(RightViewRoute.PATH)) }
    }
}