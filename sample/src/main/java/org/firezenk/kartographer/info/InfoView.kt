package org.firezenk.kartographer.info

import android.content.Context
import android.view.View
import android.widget.FrameLayout
import org.firezenk.kartographer.R
import org.firezenk.kartographer.annotations.RoutableView
import java.util.*

/**
 * Project: Kartographer
 *
 * Created by Jorge Garrido Oval, aka firezenk on 21/09/17.
 * Copyright © Jorge Garrido Oval 2017
 */
@RoutableView(params = arrayOf(String::class), requestCode = -1)
class InfoView(context: Context?) : FrameLayout(context) {

    companion object {
        fun newInstance(context: Context, uuid: UUID, text: String): InfoView {
            return InfoView(context)
        }
    }

    init {
        View.inflate(getContext(), R.layout.feature_info, this)
    }
}