package org.firezenk.kartographer.library.core

import org.firezenk.kartographer.library.Logger
import org.firezenk.kartographer.library.dsl.route
import org.firezenk.kartographer.library.types.Path
import org.firezenk.kartographer.library.types.RootRoute
import org.firezenk.kartographer.library.types.Route

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

    private val DEFAULT_HISTORY: MutableMap<Route<*>, MutableList<Route<*>>> = linkedMapOf(
            route {
                target = RootRoute()
            } to mutableListOf())

    var history: MutableMap<Route<*>, MutableList<Route<*>>> = DEFAULT_HISTORY

    var lastKnownPath: Path = ROOT_NODE

    @Suppress("UNCHECKED_CAST")
    fun <B> current(): Route<B>? {
        val leaf: Route<*>? = history.keys.firstOrNull { it.path == lastKnownPath }
        val branch: MutableList<Route<*>>? = history[leaf]

        return branch?.let {
            if (it.size < 1) {
                leaf as Route<B>?
            } else {
                it.lastOrNull() as Route<B>?
            }
        }
    }

    @Suppress("UNCHECKED_CAST")
    fun <T> payload(key: String): T? = current<Any>()?.internalParams?.get(key) as T?

    fun <B> bundle(): B? = current<B>()?.bundle

    fun clearHistory() {
        history = DEFAULT_HISTORY
    }

    fun hasHistory() = history.isNotEmpty()

    fun pathExists(route: Route<*>) = history.keys.map { it.path }.contains(route.path)

    fun pathIsValid(route: Route<*>, prev: Route<*>?) = route.path != prev?.path
}