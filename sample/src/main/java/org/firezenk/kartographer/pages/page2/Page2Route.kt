package org.firezenk.kartographer.pages.page2

import android.support.v7.app.AppCompatActivity
import org.firezenk.kartographer.annotations.RouteAnimation
import org.firezenk.kartographer.processor.interfaces.Routable
import java.util.*

/**
 * Project: Kartographer
 *
 * Created by Jorge Garrido Oval, aka firezenk on 14/12/17.
 * Copyright © Jorge Garrido Oval 2017
 */
class Page2Route : Routable {

    override fun route(context: Any, uuid: UUID, parameters: Any, viewParent: Any?, animation: RouteAnimation?) {
        (viewParent as android.view.ViewGroup).removeAllViews()

        val fm = (viewParent.context as AppCompatActivity).supportFragmentManager
        val ft = fm.beginTransaction()
        ft.replace(viewParent.id, Page2(), PATH)
        ft.commitAllowingStateLoss()
    }

    override fun path(): String = Page2Route.PATH

    companion object {
        const val PATH: String = "PAGE2"
    }
}
