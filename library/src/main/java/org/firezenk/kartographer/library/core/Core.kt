package org.firezenk.kartographer.library.core

import org.firezenk.kartographer.library.Logger
import org.firezenk.kartographer.library.dsl.route
import org.firezenk.kartographer.library.types.Path
import org.firezenk.kartographer.library.types.Route

/**
 * Project: Kartographer
 *
 * Created by Jorge Garrido Oval, aka firezenk on 21/12/17.
 * Copyright © Jorge Garrido Oval 2017
 */
class Core(val context: Any, var log: Logger? = null) {

    companion object {
        val ROOT_NODE: Path = Path("ROOT")
        val DEFAULT_HISTORY: MutableMap<Route<*>, MutableList<Route<*>>> = mutableMapOf(
                route {
                    target = Any::class
                    path = ROOT_NODE
                } to mutableListOf())
    }

    var history: MutableMap<Route<*>, MutableList<Route<*>>> = DEFAULT_HISTORY

    var lastKnownPath: Path = ROOT_NODE

    @Suppress("UNCHECKED_CAST")
    fun <B> current(): Route<B>? {
        val leaf: Route<*>? = history.keys.first { it.path == lastKnownPath }
        val branch: MutableList<Route<*>>? = history[leaf]
        return branch?.lastOrNull() as Route<B>?
    }

    @Suppress("UNCHECKED_CAST")
    fun <T> payload(key: String): T? = current<Any>()?.internalParams?.get(key) as T?

    fun clearHistory() {
        history = DEFAULT_HISTORY
    }

    fun hasHistory() = history.isNotEmpty()

    fun getHistoryLast() = history.size - 1

    fun getHistoryLastWithoutPath() {
        //history.indexOfLast { it.path == null }
    }

    fun pathExists(history: MutableMap<Route<*>, MutableList<Route<*>>>, route: Route<*>) = history.containsKey(route)

    fun pathIsValid(route: Route<*>, prev: Route<*>?) = route.path != prev?.path
}