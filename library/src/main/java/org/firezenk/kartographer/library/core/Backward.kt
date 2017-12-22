package org.firezenk.kartographer.library.core

import org.firezenk.kartographer.library.types.Path
import org.firezenk.kartographer.library.types.Route

/**
 * Project: Kartographer
 *
 * Created by Jorge Garrido Oval, aka firezenk on 21/12/17.
 * Copyright © Jorge Garrido Oval 2017
 */
class Backward(val core: Core, val move: Move) {

    fun back(block: () -> Unit): Boolean {
        core.log?.let {
            it.d(" <<--- Back")
            it.d(" History: ", core.history)
        }

        val result = when {
            core.history.isEmpty() -> false
            core.history.keys.isNotEmpty() ->
                internalBack(core.history[core.history.keys.last()]!!)
            else -> {
                core.history.remove(core.history.keys.last())
                false
            }
        }

        if (!result) block()
        return result
    }

    fun back(times: Int): Boolean {
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

    fun <B> back(route: Route<B>): Boolean {
        when {
            core.history.isEmpty() -> {
                core.log?.d("Is not possible to go back, history is empty")
                return false
            }
            core.history[core.history.keys.last()]!!.isEmpty() -> {
                val leaf = core.history[core.history.keys.last()]
                leaf?.removeAt(leaf.lastIndex)
                return back(route)
            }
            else -> {
                val branch = core.history[core.history.keys.last()]!!

                if (!branch.isEmpty()) {
                    val size = branch.size
                    for (i in size downTo 1) {
                        val prevRoute = branch.last()
                        branch.remove(prevRoute)

                        if (route.clazz == prevRoute.clazz) {
                            move.routeTo(prevRoute)
                            return true
                        }
                    }
                } else if (core.history.keys.last().clazz == route.clazz) {
                    val prevRoute = branch.last()
                    branch.remove(prevRoute)

                    move.routeTo(branch[branch.lastIndex])
                    return true
                } else {
                    core.log?.d("Is not possible to go back, there is no route like: " + route.clazz.name)
                    return false
                }

                core.history.remove(core.history.keys.last())
                return back(route)
            }
        }
    }

    private fun internalBack(branch: MutableList<Route<*>>): Boolean {
        val prevRoute = branch.last()
        branch.remove(prevRoute)
        core.log?.d(" Removing last: ", prevRoute)

        return if (branch.isNotEmpty()) {
            val nextRoute = branch.last()
            branch.remove(nextRoute)
            move.routeTo(nextRoute)
            return true
        } else false
    }

    fun backOnPath(block: () -> Unit) = core.current<Any>()?.path?.let {
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
            it.d(" History: ", core.history)
        }

        return when {
            core.history.isEmpty() -> false
            else -> {
                for (leaf in core.history.keys) {
                    if (leaf.path == path) {
                        return internalBack(core.history[leaf]!!)
                    }
                }
                false
            }
        }
    }
}