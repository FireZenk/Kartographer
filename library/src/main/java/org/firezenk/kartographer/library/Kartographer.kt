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
    constructor(internal val path: Path?, internal val route: Route<*>?, internal val viewHistory: ArrayDeque<Route<*>>) {

        override fun toString() = route.toString() + " viewHistory size: " + viewHistory.size
    }

    override fun debug(): Kartographer {
        log = Logger()
        return this;
    }

    @Suppress("UNCHECKED_CAST")
    private fun <B> routeTo(context: Any, route: Route<B>) {
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
                    if (pathIsValid(route, prev) && !pathExists(route.path)) {
                        createStartRoute(route.path);
                        createViewRoute(route);
                    } else if (pathExists(route.path)) {
                        createViewRouteOnPath(route);
                    } else {
                        createViewRoute(route);
                    }
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

    private fun internalBack(context: Any, complexRoute: ComplexRoute): Boolean {
        val prevRoute = complexRoute.viewHistory.pop()
        log?.d(" Removing last: ", prevRoute);

        return if (complexRoute.viewHistory.isNotEmpty()) {
            routeTo(context, complexRoute.viewHistory.pop());
            true;
        } else {
            routeTo(context, prevRoute);
            false;
        }
    }

    private fun pathIsValid(route: Route<*>, prev: Route<*>?): Boolean {
        return route.path != null && route.path != prev?.path
    }

    private fun pathExists(path: Path?): Boolean {
        for (i in 0..getHistoryLast()) {
            val it = history[i]
            if (it.path == path) {
                return true
            }
            val viewHistory: List<Route<*>> = history[i].viewHistory.toList()
            for (j in 0 until viewHistory.size) {
                if (viewHistory[j].path == path) {
                    return true
                }
            }
        }
        return false
    }

    override fun last(context: Any, viewParent: Any?): Boolean {
        return if (hasHistory()) {
            if (viewParent != null) {
                for (route in history[getHistoryLast()].viewHistory) {
                    route.viewParent = viewParent
                }
            }
            routeTo(context, history[getHistoryLast()].viewHistory.pop());
            true
        } else {
            false
        }
    }

    override fun <B> next(context: Any, route: Route<B>): Boolean {
        routeTo(context, route)
        return true
    }

    override fun replay(context: Any, path: Path): Boolean {
        return if (hasHistory()) {
            for (i in 0 until history.size) {
                if (history[i].path == path) {
                    routeTo(context, history[i].viewHistory.removeFirst())
                    return true
                }
            }
            false
        } else {
            false
        }
    }

    override fun back(context: Any): Boolean {
        log?.let {
            it.d(" <<--- Back")
            it.d(" History: ", history, this::getHistoryLast)
        }

        return when {
            history.isEmpty() -> false
            history[getHistoryLast()].viewHistory.isNotEmpty() -> internalBack(context, history[getHistoryLast()])
            else -> {
                history.removeAt(getHistoryLast());
                false;
            }
        }
    }

    override fun back(context: Any, times: Int): Boolean {
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

    override fun <B> back(context: Any, route: Route<B>): Boolean {
        when {
            history.isEmpty() -> {
                log?.d("Is not possible to go back, history is empty")
                return false
            }
            history[getHistoryLast()].viewHistory.isEmpty() -> {
                history.removeAt(getHistoryLast())
                return back(context, route)
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
                return back(context, route)
            }
        }
    }

    override fun back(context: Any, path: Path): Boolean {
        log?.let {
            it.d(" <<--- Back")
            it.d(" History: ", history, this::getHistoryLast)
        }

        return when {
            history.isEmpty() -> false;
            else -> {
                for (i in 0 until history.size) {
                    if (history[i].path == path) {
                        return internalBack(context, history[i])
                    }
                }
                return false
            }
        }
    }

    override fun clearHistory() = history.clear()

    override fun hasHistory() = !history.isEmpty()

    private fun createStartRoute(path: Path? = null) = history.add(ComplexRoute(path, null, ArrayDeque<Route<*>>()))

    private fun createIntermediateRoute(route: Route<*>) = history.add(ComplexRoute(route.path, route, ArrayDeque<Route<*>>()))

    private fun createViewRoute(route: Route<*>) = history[getHistoryLast()].viewHistory.addFirst(route)

    private fun createViewRouteOnPath(route: Route<*>) {
        for (i in 0 until history.size) {
            if (history[i].path == route.path) {
                history[i].viewHistory.addFirst(route)
            }
        }
    }

    private fun getHistoryLast() = history.size - 1

    private fun <B> areRoutesEqual(prev: Route<B>, next: Route<B>) =
            prev == next && (prev.bundle != null && (prev.bundle as B?)!! == next.bundle
                || prev.internalParams != null && Arrays.equals(prev.internalParams, next.internalParams))
}