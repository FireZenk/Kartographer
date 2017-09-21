package org.firezenk.kartographer.library

import java.util.ArrayDeque
import java.util.Arrays
import kotlin.collections.ArrayList

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
        routeTo(context, history.get(getHistoryLast()).viewHistory.pop());
    }

    override fun <C> back(context: C): Boolean {
        TODO("not implemented")
    }

    override fun <C> backTimes(context: C, times: Int): Boolean {
        TODO("not implemented")
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