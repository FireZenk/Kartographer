package org.firezenk.kartographer.library.core

import org.firezenk.kartographer.library.types.Path
import org.firezenk.kartographer.library.types.Route

/**
 * Project: Kartographer
 *
 * Created by Jorge Garrido Oval, aka firezenk on 21/12/17.
 * Copyright © Jorge Garrido Oval 2017
 */
class Replay(private val core: Core, private val move: Move, private val forward: Forward) {

    infix fun last(viewParent: Any?): Boolean {
        val leaf: Route<*> = core.history.keys.first { it.path == core.lastKnownPath }
        val branch: MutableList<Route<*>>? = core.history[leaf]

        return viewParent?.let {
            val route: Route<*>? = branch?.map {
                it.viewParent = viewParent
                it
            }?.firstOrNull()

            if (route == null) {
                return@let false
            } else {
                move.routeTo(route)
                return@let true
            }

        } == true
    }

    infix fun replay(path: Path): Boolean {
        val leaf: Route<*>? = core.history.keys.firstOrNull { it.path == path }

        return leaf?.let {
            val branch: MutableList<Route<*>>? = core.history[leaf]
            val route: Route<*>? = branch?.firstOrNull { it.path == path }

            return route?.let {
                branch.remove(it)
                move.routeTo(it)
                return@let true
            } == true
        } ?: false
    }

    fun <B> replayOrNext(route: Route<B>): Boolean {
        val canMove = replay(route.path)
        return if (!canMove) {
            forward.next<B>(route)
        } else canMove
    }
}