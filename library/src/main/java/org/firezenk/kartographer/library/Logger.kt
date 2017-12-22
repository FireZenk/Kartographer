package org.firezenk.kartographer.library

import org.firezenk.kartographer.library.types.Route

class Logger {

    private val TAG = "Kartographer::"

    internal fun d(actionDesc: String) {
        println(TAG + actionDesc)
    }

    internal fun d(actionDesc: String, route: Route<*>): Route<*> {
        println(TAG + actionDesc + route)
        return route
    }

    internal fun d(actionDesc: String, history: MutableMap<Route<*>, MutableList<Route<*>>>) {
        if (history.isNotEmpty() && history[history.keys.last()] != null) {
            println(TAG + actionDesc + "size: " + history.size)
            println(TAG + actionDesc + "last: " + history[history.keys.last()]?.last())
            if (history[history.keys.last()] != null && history[history.keys.last()]!!.size > 0) {
                println(TAG + actionDesc + "internal history size: " + history[history.keys.last()]!!.size)
                println(TAG + actionDesc + "internal history last: " + history[history.keys.last()]!!.last())
            }
        }
    }

    internal fun d(actionDesc: String, throwable: Throwable) {
        println(TAG + actionDesc)
        throwable.printStackTrace()
    }
}