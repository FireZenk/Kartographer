package org.firezenk.kartographer.library

class Logger {

    private val TAG = "Kartographer::"

    internal fun d(actionDesc: String) {
        println(TAG + actionDesc)
    }

    internal fun d(actionDesc: String, route: Route<*>): Route<*> {
        println(TAG + actionDesc + route)
        return route
    }

    internal fun d(actionDesc: String, history: List<Kartographer.ComplexRoute>, getHistoryLast: () -> Int) {
        if (history.isNotEmpty() && history[getHistoryLast()] != null) {
            println(TAG + actionDesc + "size: " + history.size)
            println(TAG + actionDesc + "last: " + history[getHistoryLast()])
            if (history[getHistoryLast()] != null && history[getHistoryLast()].viewHistory.size > 0) {
                println(TAG + actionDesc + "internal history size: " + history[getHistoryLast()].viewHistory.size)
                println(TAG + actionDesc + "internal history last: " + history[getHistoryLast()].viewHistory.peek())
            }
        }
    }

    internal fun d(actionDesc: String, throwable: Throwable) {
        println(TAG + actionDesc)
        throwable.printStackTrace()
    }
}