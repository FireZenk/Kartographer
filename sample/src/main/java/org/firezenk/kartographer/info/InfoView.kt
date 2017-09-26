package org.firezenk.kartographer.info

import android.content.Context
import android.view.View
import android.widget.FrameLayout
import kotlinx.android.synthetic.main.feature_info.view.*
import org.firezenk.kartographer.MainActivity
import org.firezenk.kartographer.R
import org.firezenk.kartographer.annotations.RoutableView
import org.firezenk.kartographer.library.Kartographer
import org.firezenk.kartographer.library.Route
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

        lateinit var text : String

        fun newInstance(context: Context, uuid: UUID, text: String): InfoView {
            this.text = text
            return InfoView(context)
        }
    }

    init {
        View.inflate(getContext(), R.layout.feature_info, this)

        textView.text = text

        openDetail.setOnClickListener {
            Kartographer.routeTo(getContext(), Route<Any>(
                    InfoViewRoute::class.java,
                    arrayOf("${Random().nextInt()}"),
                    (getContext() as MainActivity).getPlaceHolder())
            )
        }
    }
}