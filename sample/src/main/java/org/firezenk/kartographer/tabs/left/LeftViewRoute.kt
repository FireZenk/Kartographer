package org.firezenk.kartographer.tabs.left

import android.content.Context
import android.view.View
import android.view.ViewGroup
import org.firezenk.kartographer.annotations.RouteAnimation
import org.firezenk.kartographer.processor.exceptions.NotEnoughParametersException
import org.firezenk.kartographer.processor.exceptions.ParameterNotFoundException
import org.firezenk.kartographer.processor.interfaces.Routable
import java.util.*
import javax.annotation.Generated

@Generated("RouteProcessor")
class LeftViewRoute<T> : Routable {

    override fun route(context: Any, uuid: UUID, parameters: Array<Any>, viewParent: Any?, animation: RouteAnimation?) {
        if (parameters.isEmpty()) {
            throw NotEnoughParametersException("Need 1 params")
        }

        if (parameters[0] !is Int) {
            throw ParameterNotFoundException("Need int")
        }

        if (viewParent !is ViewGroup) {
            throw ParameterNotFoundException("Need a view parent or is not a ViewGroup")
        }

        val next: View = LeftView.newInstance(context as Context, uuid, parameters[0] as Int)
        val prev: View? = viewParent.getChildAt(viewParent.childCount - 1)

        animation?.let {
            prev?.let {
                next.alpha = 0f
                viewParent.addView(next)

                animation.animate(prev, next)

                viewParent.removeView(prev)
            } ?: replace(viewParent, next)
        } ?: replace(viewParent, next)
    }

    override fun path(): String = LeftViewRoute.PATH

    private fun replace(viewParent: ViewGroup, next: View) {
        viewParent.removeAllViews()
        viewParent.addView(next)
    }

    companion object {
        const val PATH: String = "LEFT"
    }
}