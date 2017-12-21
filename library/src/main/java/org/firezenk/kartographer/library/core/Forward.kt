package org.firezenk.kartographer.library.core

import org.firezenk.kartographer.library.types.Route

/**
 * Project: Kartographer
 *
 * Created by Jorge Garrido Oval, aka firezenk on 21/12/17.
 * Copyright © Jorge Garrido Oval 2017
 */
class Forward(val move: Move) {

    infix fun <B> next(route: Route<B>): Boolean {
        move.routeTo<B>(route)
        return true
    }

    fun <B> next(route: Route<B>, replacementParams: B): Boolean {
        move.routeTo(route.copy(replacementParams))
        return true
    }

    fun next(route: Route<Any>, replacementParams: Map<String, Any>): Boolean {
        move.routeTo(route.copy(replacementParams))
        return true
    }
}