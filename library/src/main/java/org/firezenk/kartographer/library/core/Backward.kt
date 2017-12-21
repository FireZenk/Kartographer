package org.firezenk.kartographer.library.core

import org.firezenk.kartographer.library.types.ComplexRoute
import org.firezenk.kartographer.library.types.Path
import org.firezenk.kartographer.library.types.Route

/**
 * Project: Kartographer
 *
 * Created by Jorge Garrido Oval, aka firezenk on 21/12/17.
 * Copyright © Jorge Garrido Oval 2017
 */
class Backward(val core: Core, val move: Move) {

    infix fun back(block: () -> Unit): Boolean {
        core.log?.let {
            it.d(" <<--- Back")
            it.d(" History: ", core.history, core::getHistoryLast)
        }

        val result = when {
            core.history.isEmpty() -> false
            core.history[core.getHistoryLastWithoutPath()].viewHistory.isNotEmpty() ->
                internalBack(core.history[core.getHistoryLastWithoutPath()])
            else -> {
                core.history.removeAt(core.getHistoryLast())
                false
            }
        }

        if (!result) block()
        return result
    }

    infix fun back(times: Int): Boolean {
        try {
            for (i in 0 until times) {
                if (!back({})) {
                    return false
                }
            }
            return true
        } catch (e: Exception) {
            core.log?.d("Is not possible to go back " + times +
                    " times, the history length is " + core.history.size)
            core.log?.d(e.message!!)
            return false
        }
    }

    infix fun <B> back(route: Route<B>): Boolean {
        when {
            core.history.isEmpty() -> {
                core.log?.d("Is not possible to go back, history is empty")
                return false
            }
            core.history[core.getHistoryLast()].viewHistory.isEmpty() -> {
                core.history.removeAt(core.getHistoryLast())
                return back(route)
            }
            else -> {
                val complexRoute = core.history[core.getHistoryLast()]

                if (!complexRoute.viewHistory.isEmpty()) {
                    val size = complexRoute.viewHistory.size
                    for (i in size downTo 1) {
                        val prevRoute = complexRoute.viewHistory.pop()
                        if (route.clazz == prevRoute.clazz) {
                            move.routeTo(prevRoute)
                            return true
                        }
                    }
                } else if (complexRoute.route!!.clazz == route.clazz) {
                    core.history.removeAt(core.getHistoryLast())
                    move.routeTo(complexRoute.route)
                    return true
                } else {
                    core.log?.d("Is not possible to go back, there is no route like: " + route.clazz.name)
                    return false
                }
                core.history.removeAt(core.getHistoryLast())
                return back(route)
            }
        }
    }

    private fun internalBack(complexRoute: ComplexRoute): Boolean {
        val prevRoute = complexRoute.viewHistory.pop()
        core.log?.d(" Removing last: ", prevRoute)

        return if (complexRoute.viewHistory.isNotEmpty()) {
            move.routeTo(complexRoute.viewHistory.pop())
            return true
        } else false
    }

    infix fun backOnPath(block: () -> Unit) = core.current<Any>()?.path?.let {
        if (!internalBackOnPath(it)) {
            block()
            return@let true
        } else {
            return@let back(block)
        }
    } ?: false

    private fun internalBackOnPath(path: Path): Boolean {
        core.log?.let {
            it.d(" <<--- Back")
            it.d(" History: ", core.history, core::getHistoryLast)
        }

        return when {
            core.history.isEmpty() -> false
            else -> {
                for (i in 0 until core.history.size) {
                    if (core.history[i].path == path) {
                        return internalBack(core.history[i])
                    }
                }
                false
            }
        }
    }
}