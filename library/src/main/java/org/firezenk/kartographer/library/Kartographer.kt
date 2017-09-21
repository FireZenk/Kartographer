package org.firezenk.kartographer.library

import java.util.ArrayDeque
import java.util.Arrays
import kotlin.collections.ArrayList
import javax.management.Query.times



/**
 * Project: Kartographer
 *
 * Created by Jorge Garrido Oval, aka firezenk on 20/09/17.
 * Copyright © Jorge Garrido Oval 2017
 */
class Kartographer : IKartographer {

    companion object {
        private var INSTANCE: Kartographer = Kartographer()
    }

    private val history: ArrayList<ComplexRoute> = ArrayList()
    private var log: Logger? = null

    internal class ComplexRoute internal
    constructor(internal val route: Route<*>?, internal val viewHistory: ArrayDeque<Route<*>>) {

        override fun toString(): String {
            return route.toString() + " viewHistory size: " + viewHistory.size
        }
    }

    fun get() : Kartographer = INSTANCE

    override fun debug(): Kartographer {
        log = Logger()
        return INSTANCE;
    }

    override fun <C, B> routeTo(context: C, route: Route<B>) {
        TODO("not implemented")
    }

    override fun <C> routeToLast(context: C, viewParent: Any?) {
        if (viewParent != null) {
            for (route in history[getHistoryLast()].viewHistory) {
                route.viewParent = viewParent
            }
        }
        routeTo(context, history.get(getHistoryLast()).viewHistory.pop());
    }

    override fun <C> back(context: C): Boolean {
        log?.d(" <<--- Back")
        log?.d(" History: ", history, this::getHistoryLast)

        if (history.isEmpty()) {
            return false;
        } else if (!history.get(getHistoryLast()).viewHistory.isEmpty()) {
            log?.d(" Removing last: ", history.get(getHistoryLast()).viewHistory.pop());

            if (!history.get(getHistoryLast()).viewHistory.isEmpty()) {
                routeTo(context, history.get(getHistoryLast()).viewHistory.pop());
                return true;
            }
        } else {
            history.removeAt(getHistoryLast());
            return false;
        }

        return back(context);
    }

    override fun <C> backTimes(context: C, times: Int): Boolean {
        try {
            for (i in 0..times - 1) {
                if (!back(context)) {
                    return false
                }
            }
            return true
        } catch (e: Exception) {
            System.out.println("Is not possible to go back " + times +
                    " times, the history length is " + history.size())
            log?.d(e.message!!)
            return false
        }
    }

    override fun <C, B> backTo(context: C, route: Route<B>): Boolean {
        TODO("not implemented")
    }

    override fun clearHistory() {
        TODO("not implemented")
    }

    override fun hasHistory(): Boolean {
        TODO("not implemented")
    }

    private fun createStartRoute() {
        history.add(ComplexRoute(null, ArrayDeque<Route<*>>()))
    }

    private fun createIntermediateRoute(route: Route<*>) {
        history.add(ComplexRoute(route, ArrayDeque<Route<*>>()))
    }

    private fun createViewRoute(route: Route<*>) {
        history[getHistoryLast()].viewHistory.addFirst(route)
    }

    private fun getHistoryLast(): Int {
        return history.size - 1
    }

    private fun <B> areRoutesEqual(prev: Route<B>, next: Route<B>): Boolean {
        return prev == next && (prev.bundle != null && (prev.bundle as B?)!!.equals(next.bundle)
                || prev.internalParams != null && Arrays.equals(prev.internalParams, next.internalParams))
    }
}