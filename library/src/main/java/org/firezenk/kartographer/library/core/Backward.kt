package org.firezenk.kartographer.library.core

import org.firezenk.kartographer.library.types.Route

/**
 * Project: Kartographer
 *
 * Created by Jorge Garrido Oval, aka firezenk on 21/12/17.
 * Copyright © Jorge Garrido Oval 2017
 */
class Backward(private val core: Core, private val move: Move) {

    fun back(block: () -> Unit): Boolean {
        core.log?.let {
            it.d(" <<--- Back")
            it.d(" History: ", core.history)
        }

        if (core.history.size <= 1) {
            block()
            return false
        }

        var leaf: Route<*> = core.history.keys.last()
        core.history.remove(leaf)

        leaf = core.history.keys.last()
        val branch: MutableList<Route<*>> = core.history[leaf]!!

        move.routeTo(branch.last())
        return true
    }

    fun backOnPath(block: () -> Unit): Boolean {
        core.log?.let {
            it.d(" <<--- Back on path")
            it.d(" History: ", core.history)
        }

        if (core.history.size <= 1 && core.history[core.history.keys.last()]!!.size <= 1) {
            block()
            return false
        }

        val leaf: Route<*> = core.history.keys.first { it.path == core.lastKnownPath }
        val branch: MutableList<Route<*>> = core.history[leaf]!!

        branch.removeAt(branch.lastIndex)

        return if (branch.lastIndex > -1) {
            move.routeTo(branch.removeAt(branch.lastIndex))
            true
        } else {
            block()
            false
        }
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
}