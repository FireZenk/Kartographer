package org.firezenk.kartographer.library

import org.firezenk.kartographer.library.exceptions.NotEnoughParametersException
import org.firezenk.kartographer.library.exceptions.ParameterNotFoundException
import java.util.ArrayDeque
import java.util.Arrays
import kotlin.collections.ArrayList

/**
 * Project: Kartographer
 *
 * Created by Jorge Garrido Oval, aka firezenk on 20/09/17.
 * Copyright © Jorge Garrido Oval 2017
 */
object Kartographer : IKartographer {

    private val history: ArrayList<ComplexRoute> = ArrayList()
    private var log: Logger? = null

    internal class ComplexRoute internal
    constructor(internal val route: Route<*>?, internal val viewHistory: ArrayDeque<Route<*>>) {

        override fun toString() = route.toString() + " viewHistory size: " + viewHistory.size
    }

    override fun debug(): Kartographer {
        log = Logger()
        return this;
    }

    @Suppress("UNCHECKED_CAST")
    override fun <B> routeTo(context: Any, route: Route<B>) {
        val prev: Route<B>? = if (history.isEmpty()) null else history[history.size - 1].viewHistory.peek() as Route<B>?
        try {
            if (prev == null || route.viewParent == null || !areRoutesEqual(prev, route)) {
                log?.let {
                    it.d(" --->> Next")
                    it.d(" Navigating to: ", route)
                }

                if (route.bundle != null) {
                    (route.clazz.newInstance() as Routable<B>)
                            .route(context, route.uuid, route.bundle as B, route.viewParent);
                } else {
                    (route.clazz.newInstance() as org.firezenk.kartographer.processor.interfaces.Routable)
                            .route(context, route.uuid, route.params as Array<Any>, route.viewParent);
                }

                if (history.size == 0) {
                    createStartRoute();
                }

                if (route.viewParent == null) {
                    createIntermediateRoute(route);
                } else {
                    createViewRoute(route);
                }
            }
        } catch (e: Exception) {
            when(e) {
                is ClassCastException -> log?.d(" Params has to be instance of Object[] or Android's Bundle ", e)
                is ParameterNotFoundException,
                is NotEnoughParametersException,
                is InstantiationException,
                is IllegalAccessException,
                is org.firezenk.kartographer.processor.exceptions.NotEnoughParametersException,
                is org.firezenk.kartographer.processor.exceptions.ParameterNotFoundException -> {
                    log?.d(" Navigation error; ", e.fillInStackTrace());
                }
                else -> throw e
            }
        }
    }

    override fun routeToLast(context: Any, viewParent: Any?) {
        if (viewParent != null) {
            for (route in history[getHistoryLast()].viewHistory) {
                route.viewParent = viewParent
            }
        }
        routeTo(context, history[getHistoryLast()].viewHistory.pop());
    }

    override fun back(context: Any): Boolean {
        log?.let {
            it.d(" <<--- Back")
            it.d(" History: ", history, this::getHistoryLast)
        }

        when {
            history.isEmpty() -> {
                return false;
            }
            !history[getHistoryLast()].viewHistory.isEmpty() -> {
                val prevRoute = history[getHistoryLast()].viewHistory.pop()
                log?.d(" Removing last: ", prevRoute);

                if (!history[getHistoryLast()].viewHistory.isEmpty()) {
                    routeTo(context, history[getHistoryLast()].viewHistory.pop());
                    return true;
                } else {
                    routeTo(context, prevRoute);
                    return false;
                }
            }
            else -> {
                history.removeAt(getHistoryLast());
                return false;
            }
        }
    }

    override fun backTimes(context: Any, times: Int): Boolean {
        try {
            for (i in 0 until times) {
                if (!back(context)) {
                    return false
                }
            }
            return true
        } catch (e: Exception) {
            log?.d("Is not possible to go back " + times +
                    " times, the history length is " + history.size)
            log?.d(e.message!!)
            return false
        }
    }

    override fun <B> backTo(context: Any, route: Route<B>): Boolean {
        when {
            history.isEmpty() -> {
                log?.d("Is not possible to go back, history is empty")
                return false
            }
            history[getHistoryLast()].viewHistory.isEmpty() -> {
                history.removeAt(getHistoryLast())
                return backTo(context, route)
            }
            else -> {
                val complexRoute = history[getHistoryLast()]

                if (!complexRoute.viewHistory.isEmpty()) {
                    val size = complexRoute.viewHistory.size
                    for (i in size downTo 1) {
                        val prevRoute = complexRoute.viewHistory.pop()
                        if (route.clazz == prevRoute.clazz) {
                            this.routeTo(context, prevRoute)
                            return true
                        }
                    }
                } else if (complexRoute.route!!.clazz == route.clazz) {
                    history.removeAt(getHistoryLast())
                    this.routeTo(context, complexRoute.route)
                    return true
                } else {
                    log?.d("Is not possible to go back, there is no route like: " + route.clazz.name)
                    return false
                }
                history.removeAt(getHistoryLast())
                return backTo(context, route)
            }
        }
    }

    override fun clearHistory() = history.clear()

    override fun hasHistory() = !history.isEmpty()

    private fun createStartRoute() = history.add(ComplexRoute(null, ArrayDeque<Route<*>>()))

    private fun createIntermediateRoute(route: Route<*>) = history.add(ComplexRoute(route, ArrayDeque<Route<*>>()))

    private fun createViewRoute(route: Route<*>) = history[getHistoryLast()].viewHistory.addFirst(route)

    private fun getHistoryLast() = history.size - 1

    private fun <B> areRoutesEqual(prev: Route<B>, next: Route<B>) =
            prev == next && (prev.bundle != null && (prev.bundle as B?)!!.equals(next.bundle)
                || prev.internalParams != null && Arrays.equals(prev.internalParams, next.internalParams))
}