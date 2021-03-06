package org.firezenk.kartographer.library.core

import org.firezenk.kartographer.library.Logger
import org.firezenk.kartographer.library.dsl.route
import org.firezenk.kartographer.library.types.*

/**
 * Project: Kartographer
 *
 * Created by Jorge Garrido Oval, aka firezenk on 21/12/17.
 * Copyright © Jorge Garrido Oval 2017
 */
class Core(var context: Any, var log: Logger? = null) {

    companion object {
        val ROOT_NODE: Path = Path("ROOT")
    }

    var history: MutableMap<Route, MutableList<Route>> = generateEmptyHistory()

    var lastKnownPath: Path = ROOT_NODE

    fun current(): Route? {
        val leaf: Route? = lastLeaf()
        val branch: MutableList<Route>? = history[leaf]

        return branch?.let {
            if (it.size < 1) {
                leaf
            } else {
                it.lastOrNull()
            }
        }
    }

    @Suppress("UNCHECKED_CAST")
    fun <T> payload(key: String): T? = current()?.let { (it as ViewRoute).params[key] as T? }

    @Suppress("UNCHECKED_CAST")
    fun <B> bundle(): B? = current()?.let { (it as ContextRoute<B>).bundle }

    fun clearHistory() {
        history = generateEmptyHistory()
    }

    fun hasHistory() = history.isNotEmpty()

    internal fun pathExists(route: Route) = history.keys.map { it.path }.contains(route.path)

    internal fun pathIsValid(route: Route, prev: Route?) = route.path != prev?.path

    internal fun lastLeaf() = history.keys.firstOrNull { it.path == lastKnownPath }

    internal fun lastLeaf(path: Path) = history.keys.firstOrNull { it.path == path }

    private fun generateEmptyHistory(): MutableMap<Route, MutableList<Route>> = linkedMapOf(
            route {
                target = RootRoute()
                anchor = Any()
            } to mutableListOf())
}