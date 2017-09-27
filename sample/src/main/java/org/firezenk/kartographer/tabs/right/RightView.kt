package org.firezenk.kartographer.tabs.right

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import kotlinx.android.synthetic.main.righttab_view.view.*
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
@RoutableView(path = "RIGHT", params = arrayOf(Int::class), requestCode = -1)
class RightView(context: Context?) : FrameLayout(context) {

    companion object {

        var counter : Int = 0

        fun newInstance(context: Context, uuid: UUID, counter: Int): RightView {
            this.counter = counter
            return RightView(context)
        }
    }

    init {
        View.inflate(getContext(), R.layout.righttab_view, this)

        text2.text = "${RightViewRoute.PATH}: $counter"

        setOnClickListener {
            val route = Route<Any>(RightViewRoute::class.java,
                    arrayOf(++counter), parent as ViewGroup, Path(RightViewRoute.PATH))
            Kartographer.next(getContext(), route)
        }
    }
}