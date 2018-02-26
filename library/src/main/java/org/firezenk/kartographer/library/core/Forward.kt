package org.firezenk.kartographer.library.core

import org.firezenk.kartographer.library.types.*

/**
 * Project: Kartographer
 *
 * Created by Jorge Garrido Oval, aka firezenk on 21/12/17.
 * Copyright © Jorge Garrido Oval 2017
 */
class Forward(private val move: Move) {

    fun next(route: Route) = move.routeTo(route)

    fun next(route: ViewRoute, replacementParams: Map<String, Any>) = move.routeTo(route.copy(replacementParams))

    fun next(route: ExternalRoute, replacementParams: Map<String, Any>) = move.routeTo(route.copy(replacementParams))

    fun <B> next(route: ContextRoute<B>, replacementParams: B) = move.routeTo(route.copy(replacementParams))
}