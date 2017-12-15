package org.firezenk.kartographer.pages.page2

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.page_view.*
import org.firezenk.kartographer.R
import org.firezenk.kartographer.SampleApplication
import org.firezenk.kartographer.library.Kartographer
import org.firezenk.kartographer.library.dsl.route
import javax.inject.Inject

/**
 * Project: Kartographer
 *
 * Created by Jorge Garrido Oval, aka firezenk on 14/12/17.
 * Copyright © Jorge Garrido Oval 2017
 */
class Page2 : Fragment() {

    @Inject lateinit var router: Kartographer

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return View.inflate(context, R.layout.page_view, null)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val counter = arguments!!.getInt("counter")

        SampleApplication.component.injectTo(this)

        text2.text = "${Page2Route.PATH}: $counter"

        text2.setOnClickListener {
            router next route<Any> {
                target = Page2Route::class
                params = mapOf("counter" to counter + 10)
                anchor = getView()!!.parent
            }
        }
    }
}