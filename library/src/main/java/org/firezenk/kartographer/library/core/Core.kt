package org.firezenk.kartographer.library.core

import org.firezenk.kartographer.library.Logger
import org.firezenk.kartographer.library.types.ComplexRoute
import org.firezenk.kartographer.library.types.Path
import org.firezenk.kartographer.library.types.Route

/**
 * Project: Kartographer
 *
 * Created by Jorge Garrido Oval, aka firezenk on 21/12/17.
 * Copyright © Jorge Garrido Oval 2017
 */
class Core(val context: Any, var log: Logger? = null) {

    val history: ArrayList<ComplexRoute> = ArrayList()
    var lastKnownPath: Path? = null

    @Suppress("UNCHECKED_CAST")
    fun <B> current(): Route<B>? {
        val route: ComplexRoute? = history.firstOrNull { it.path == lastKnownPath }
        return route?.let {
            if (it.viewHistory.isNotEmpty()) {
                it.viewHistory.first as Route<B>?
            } else {
                it.route as Route<B>?
            }
        }
    }

    @Suppress("UNCHECKED_CAST")
    fun <T> payload(key: String): T? = current<Any>()?.internalParams?.get(key) as T?

    fun clearHistory() = history.clear()

    fun hasHistory() = !history.isEmpty()

    fun getHistoryLast() = history.size - 1

    fun getHistoryLastWithoutPath() = history.indexOfLast { it.path == null }
}