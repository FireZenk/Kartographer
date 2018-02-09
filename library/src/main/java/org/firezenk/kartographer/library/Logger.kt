package org.firezenk.kartographer.library

import org.firezenk.kartographer.library.types.Route

class Logger(private val logReader: (String) -> Unit = ::println) {

    private val TAG = "Kartographer::"

    internal fun d(actionDesc: String) {
        logReader(TAG + actionDesc)
    }

    internal fun d(actionDesc: String, route: Route): Route {
        logReader(TAG + actionDesc + route)
        return route
    }

    internal fun d(actionDesc: String, history: MutableMap<Route, MutableList<Route>>) {
        if (history.isNotEmpty() && history[history.keys.last()] != null) {
            logReader(TAG + actionDesc + "size: " + history.size)
            logReader(TAG + actionDesc + "last: " + history[history.keys.last()]?.lastOrNull())
            if (history[history.keys.last()] != null && history[history.keys.last()]!!.size > 0) {
                logReader(TAG + actionDesc + "internal history size: " + history[history.keys.lastOrNull()]?.size)
                logReader(TAG + actionDesc + "internal history last: " + history[history.keys.lastOrNull()]?.lastOrNull())
            }
        }
    }

    internal fun d(actionDesc: String, throwable: Throwable) {
        logReader(TAG + actionDesc)
        throwable.printStackTrace()
    }
}