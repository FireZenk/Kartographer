package org.firezenk.kartographer.info

import android.content.Context
import android.view.View
import android.widget.FrameLayout
import kotlinx.android.synthetic.main.feature_info.view.*
import org.firezenk.kartographer.R
import org.firezenk.kartographer.annotations.RoutableView

/**
 * Project: Kartographer
 *
 * Created by Jorge Garrido Oval, aka firezenk on 21/09/17.
 * Copyright © Jorge Garrido Oval 2017
 */
@RoutableView(params = arrayOf(), requestCode = -1)
class InfoView(context: Context?) : FrameLayout(context) {

    init {
        View.inflate(getContext(), R.layout.feature_info, this)

        openDetail.setOnClickListener({
            val params = arrayOfNulls<Any>(3)
            params[0] = "This is the detail param 0"
            params[1] = " and this is the detail param 1 "
            params[2] = 101

            //Kartographer.get().routeTo(getContext(), Route(DetailActivityRoute::class.java, params))
        })
    }
}