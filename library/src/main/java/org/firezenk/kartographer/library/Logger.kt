package org.firezenk.kartographer.library

class Logger {

    private val DEBUG_TAG = "Kartographer::"

    internal fun d(actionDesc: String) {
        println(DEBUG_TAG + actionDesc)
    }

    internal fun d(actionDesc: String, route: Route<*>): Route<*> {
        println(DEBUG_TAG + actionDesc + route)
        return route
    }

    internal fun d(actionDesc: String, history: ArrayList<Kartographer.ComplexRoute>,
                    getHistoryLast: () -> Int): ArrayList<Kartographer.ComplexRoute> {
        if (history.size > 0 && history[getHistoryLast()] != null) {
            println(DEBUG_TAG + actionDesc + "size: " + history.size)
            println(DEBUG_TAG + actionDesc + "last: " + history[getHistoryLast()])
            if (history[getHistoryLast()] != null && history[getHistoryLast()].viewHistory.size > 0) {
                println(DEBUG_TAG + actionDesc + "internal history size: " + history[getHistoryLast()].viewHistory.size)
                println(DEBUG_TAG + actionDesc + "internal history last: " + history[getHistoryLast()].viewHistory.peek())
            }
        }
        return history
    }

    internal fun d(actionDesc: String, throwable: Throwable) {
        println(DEBUG_TAG + actionDesc)
        throwable.printStackTrace()
    }
}