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

    private val DEFAULT_HISTORY: MutableMap<Route, MutableList<Route>> = linkedMapOf(
            route {
                target = RootRoute()
            } to mutableListOf())

    var history: MutableMap<Route, MutableList<Route>> = DEFAULT_HISTORY

    var lastKnownPath: Path = ROOT_NODE

    fun current(): ViewRoute? {
        val leaf: Route? = history.keys.firstOrNull { it.path == lastKnownPath }
        val branch: MutableList<Route>? = history[leaf]

        return branch?.let {
            if (it.size < 1) {
                leaf as ViewRoute?
            } else {
                it.lastOrNull() as ViewRoute?
            }
        }
    }

    @Suppress("UNCHECKED_CAST")
    fun <B> current(): ContextRoute<B>? {
        val leaf: Route? = history.keys.firstOrNull { it.path == lastKnownPath }
        val branch: MutableList<Route>? = history[leaf]

        return branch?.let {
            if (it.size < 1) {
                leaf as ContextRoute<B>?
            } else {
                it.lastOrNull() as ContextRoute<B>?
            }
        }
    }

    @Suppress("UNCHECKED_CAST")
    fun <T> payload(key: String): T? = current()?.params?.get(key) as T?

    fun <B> bundle(): B? = current<B>()?.bundle

    fun clearHistory() {
        history = DEFAULT_HISTORY
    }

    fun hasHistory() = history.isNotEmpty()

    fun pathExists(route: Route) = history.keys.map { it.path }.contains(route.path)

    fun pathIsValid(route: Route, prev: Route?) = route.path != prev?.path
}