package org.firezenk.kartographer.library.core

import org.firezenk.kartographer.library.types.Path
import org.firezenk.kartographer.library.types.Route

/**
 * Project: Kartographer
 *
 * Created by Jorge Garrido Oval, aka firezenk on 21/12/17.
 * Copyright © Jorge Garrido Oval 2017
 */
class Replay(val core: Core, val move: Move, val forward: Forward) {

    infix fun last(viewParent: Any?): Boolean {
        return if (core.hasHistory()) {
            if (viewParent != null) {
                for (route in core.history[core.getHistoryLast()].viewHistory) {
                    route.viewParent = viewParent
                }
            }
            val route = core.history.firstOrNull()?.viewHistory?.firstOrNull()

            return if (route != null) {
                move.routeTo(route)
                return true
            } else false
        } else false
    }

    infix fun replay(path: Path): Boolean {
        return if (core.hasHistory()) {
            for (i in 0 until core.history.size) {
                if (core.history[i].path == path && core.history[i].viewHistory.isNotEmpty()) {
                    move.routeTo(core.history[i].viewHistory.pop())
                    return true
                }
            }
            return false
        } else false
    }

    fun <B> replayOrNext(route: Route<B>): Boolean {
        val canMove = route.path?.let { replay(route.path!!) } ?: false
        return if (!canMove) {
            forward.next<B>(route)
        } else canMove
    }
}