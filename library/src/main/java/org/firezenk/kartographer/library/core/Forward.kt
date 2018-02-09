package org.firezenk.kartographer.library.core

import org.firezenk.kartographer.library.types.ContextRoute
import org.firezenk.kartographer.library.types.ExternalRoute
import org.firezenk.kartographer.library.types.Route
import org.firezenk.kartographer.library.types.ViewRoute

/**
 * Project: Kartographer
 *
 * Created by Jorge Garrido Oval, aka firezenk on 21/12/17.
 * Copyright © Jorge Garrido Oval 2017
 */
class Forward(private val move: Move) {

    fun next(route: Route) = when(route) {
        is ViewRoute -> next(route)
        is ContextRoute<*> -> next(route)
        is ExternalRoute -> next(route)
    }

    private fun next(route: ViewRoute) = move.routeTo(route)

    private fun next(route: ExternalRoute) = move.routeTo(route)

    private fun <B> next(route: ContextRoute<B>) = move.routeTo<B>(route)

    fun next(route: ViewRoute, replacementParams: Map<String, Any>) = move.routeTo(route.copy(replacementParams))

    fun <B> next(route: ContextRoute<B>, replacementParams: B) = move.routeTo(route.copy(replacementParams))
}