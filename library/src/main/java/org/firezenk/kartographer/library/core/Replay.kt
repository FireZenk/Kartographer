package org.firezenk.kartographer.library.core

import org.firezenk.kartographer.library.types.Path
import org.firezenk.kartographer.library.types.Route
import org.firezenk.kartographer.library.types.ViewRoute

/**
 * Project: Kartographer
 *
 * Created by Jorge Garrido Oval, aka firezenk on 21/12/17.
 * Copyright © Jorge Garrido Oval 2017
 */
class Replay(private val core: Core, private val move: Move, private val forward: Forward) {

    infix fun last(viewParent: Any?): Boolean = core.lastLeaf()?.let {
        val branch: MutableList<Route>? = core.history[it]
        return@let viewParent?.let {
            val route: Route? = branch?.map {
                if (it is ViewRoute) {
                    it.viewParent = viewParent
                    it
                } else null
            }?.firstOrNull()

            return if (route == null) false else move.routeTo(route)
        } == true
    } == true

    infix fun replay(path: Path): Boolean = core.lastLeaf(path)?.let {
        val branch: MutableList<Route> = core.history[it]!!
        return if (branch.lastIndex > -1) {
            move.routeTo(branch.removeAt(branch.lastIndex))
        } else false
    } == true

    infix fun replayOrNext(route: Route): Boolean = with(replay(route.path)) {
        if (!this) forward.next(route) else this
    }
}