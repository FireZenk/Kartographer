package org.firezenk.kartographer.pages.page2

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import org.firezenk.kartographer.annotations.RouteAnimation
import org.firezenk.kartographer.library.Routable
import java.util.*

/**
 * Project: Kartographer
 *
 * Created by Jorge Garrido Oval, aka firezenk on 14/12/17.
 * Copyright © Jorge Garrido Oval 2017
 */
class Page2Route<B> : Routable<B> {
    override fun route(context: Any, uuid: UUID, parameters: B, viewParent: Any?, animation: RouteAnimation?) {
        (viewParent as android.view.ViewGroup).removeAllViews()

        val fragment = Page2()
        fragment.arguments = parameters as Bundle

        val fm = (viewParent.context as AppCompatActivity).supportFragmentManager
        val ft = fm.beginTransaction()
        ft.replace(viewParent.id, fragment, PATH)
        ft.commitAllowingStateLoss()
    }

    override fun path(): String = Page2Route.PATH

    companion object {
        const val PATH: String = "PAGE2"
    }
}
