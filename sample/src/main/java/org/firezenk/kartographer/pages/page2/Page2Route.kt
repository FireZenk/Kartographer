package org.firezenk.kartographer.pages.page2

import android.os.Bundle
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

    override fun route(context: Any, uuid: UUID, parameters: Array<Any>, viewParent: Any?, animation: RouteAnimation?) {
        if (parameters.isEmpty()) {
            throw org.firezenk.kartographer.processor.exceptions.NotEnoughParametersException("Need 1 params")
        }

        if (parameters[0] !is kotlin.Int) {
            throw org.firezenk.kartographer.processor.exceptions.ParameterNotFoundException("Need int")
        }

        if (viewParent !is android.view.ViewGroup) {
            throw org.firezenk.kartographer.processor.exceptions.ParameterNotFoundException("Need a view parent or is not a ViewGroup")
        }

        viewParent.removeAllViews()

        val fragment = Page2()
        val bundle = Bundle()

        bundle.putInt("counter", parameters[0] as Int)
        fragment.arguments = bundle

        val fm = (viewParent.context as AppCompatActivity).supportFragmentManager
        val ft = fm.beginTransaction()
        ft.add(viewParent.id, fragment, PATH)
        ft.commit()
    }

    override fun path(): String = Page2Route.PATH

    companion object {
        const val PATH: String = "PAGE2"
    }
}
