package org.firezenk.kartographer.pages.page2.subpage

import android.content.Context
import android.view.View
import android.widget.FrameLayout
import kotlinx.android.synthetic.main.page_view.view.*
import org.firezenk.kartographer.R
import org.firezenk.kartographer.SampleApplication
import org.firezenk.kartographer.annotations.RoutableView
import org.firezenk.kartographer.library.Kartographer
import javax.inject.Inject

/**
 * Project: Kartographer
 *
 * Created by Jorge Garrido Oval, aka firezenk on 14/12/17.
 * Copyright © Jorge Garrido Oval 2017
 */
@RoutableView(path = "PAGE2")
class Subpage(context: Context?) : FrameLayout(context) {

    @Inject lateinit var router: Kartographer

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        View.inflate(context, R.layout.page_view, this)

        SampleApplication.component.injectTo(this)

        text2.text = "SUBPAGE"
    }
}