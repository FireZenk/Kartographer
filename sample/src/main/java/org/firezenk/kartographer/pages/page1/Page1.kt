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
import org.firezenk.kartographer.library.types.Path
import javax.inject.Inject

/**
 * Project: Kartographer
 *
 * Created by Jorge Garrido Oval, aka firezenk on 14/12/17.
 * Copyright © Jorge Garrido Oval 2017
 */
@RoutableView(path = "PAGE1")
class Page1(context: Context?) : FrameLayout(context) {

    @Inject lateinit var router: Kartographer

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        View.inflate(context, R.layout.page_view, this)

        SampleApplication.component.injectTo(this)

        var part: String = router.payload<String>("part")!!
        var counter: Int = router.payload<Int>("counter")!!

        part += " -> " + ++counter
        text2.text = context.getString(R.string.route, Page1Route.PATH, part)

        setOnClickListener {
            router next route {
                target = Page1Route::class
                path = Path(Page1Route.PATH)
                params = mapOf("part" to part, "counter" to counter)
                anchor = parent
                animation = CrossFade()
            }
        }
    }
}