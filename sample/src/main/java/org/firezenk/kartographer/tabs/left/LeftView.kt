package org.firezenk.kartographer.tabs.left

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import kotlinx.android.synthetic.main.lefttab_view.view.*
import org.firezenk.kartographer.R
import org.firezenk.kartographer.annotations.RoutableView
import org.firezenk.kartographer.library.Kartographer
import org.firezenk.kartographer.library.Path
import org.firezenk.kartographer.library.Route
import java.util.*

/**
 * Project: Kartographer
 *
 * Created by Jorge Garrido Oval, aka firezenk on 26/09/17.
 * Copyright © Jorge Garrido Oval 2017
 */
@RoutableView(path = "LEFT", params = arrayOf(Int::class), requestCode = -1)
class LeftView(context: Context?) : FrameLayout(context) {

    companion object {

        var counter : Int = 0

        fun newInstance(context: Context, uuid: UUID, counter: Int): LeftView {
            this.counter = counter
            return LeftView(context)
        }
    }

    init {
        View.inflate(getContext(), R.layout.lefttab_view, this)

        text1.text = "${LeftViewRoute.PATH}: $counter"

        setOnClickListener {
            val route = Route<Any>(LeftViewRoute::class.java,
                    arrayOf(++counter), parent as ViewGroup, Path(LeftViewRoute.PATH))
            Kartographer.next(getContext(), route)
        }
    }
}