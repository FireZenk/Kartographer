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
class Kartographer(private val context: Any) : IKartographer {

    private val history: ArrayList<ComplexRoute> = ArrayList()
    private var log: Logger? = null
    private var lastKnownPath: Path? = null

    internal class ComplexRoute internal
    constructor(internal val path: Path?, internal val route: Route<*>?, internal val viewHistory: ArrayDeque<Route<*>>) {

        override fun toString() = route.toString() + " viewHistory size: " + viewHistory.size
    }

    override fun debug(): Kartographer {
        log = Logger()
        return this
    }

    @Suppress("UNCHECKED_CAST")
    private fun <B> routeTo(route: Route<B>) {
        val prev: Route<B>? = if (history.isEmpty()) null else history[history.size - 1].viewHistory.peek() as Route<B>?
        try {
            if (prev == null || route.viewParent == null || !areRoutesEqual(prev, route)) {
                log?.let {
                    it.d(" --->> Next")
                    it.d(" Navigating to: ", route)
                }

                lastKnownPath = route.path

                if (history.size == 0) {
                    createStartRoute()
                }

                if (route.viewParent == null) {
                    createIntermediateRoute(route)
                } else {
                    if (pathIsValid(route, prev) && !pathExists(route.path)) {
                        createStartRoute(route.path)
                        createViewRoute(route)
                    } else if (pathExists(route.path)) {
                        createViewRouteOnPath(route)
                    } else {
                        createViewRoute(route)
                    }
                }

                createView(route)
            }
        } catch (e: Throwable) {
            when(e) {
                is ClassCastException -> log?.d(" Params has to be instance of Object[] or Android's Bundle ", e)
                is ParameterNotFoundException,
                is NotEnoughParametersException,
                is InstantiationException,
                is IllegalAccessException,
                is org.firezenk.kartographer.processor.exceptions.NotEnoughParametersException,
                is org.firezenk.kartographer.processor.exceptions.ParameterNotFoundException -> {
                    log?.d(" Navigation error: ", e.fillInStackTrace())
                }
                else -> throw e
            }
        }
    }

    @Suppress("UNCHECKED_CAST")
    private fun <B> createView(route: Route<B>) {
        if (route.bundle != null) {
            try {
                (route.clazz.newInstance() as Routable<B>)
                        .route(context, route.uuid, route.bundle as B, route.viewParent, route.animation)
            } catch (e: ClassCastException) {
                (route.clazz.newInstance() as org.firezenk.kartographer.processor.interfaces.Routable)
                        .route(context, route.uuid, route.bundle!!, route.viewParent, route.animation)
            }
        } else {
            (route.clazz.newInstance() as org.firezenk.kartographer.processor.interfaces.Routable)
                    .route(context, route.uuid, (route.params as Map<String, Any>).values.toTypedArray(), route.viewParent, route.animation)
        }
    }

    private fun internalBack(complexRoute: ComplexRoute): Boolean {
        val prevRoute = complexRoute.viewHistory.pop()
        log?.d(" Removing last: ", prevRoute)

        return if (complexRoute.viewHistory.isNotEmpty()) {
            routeTo(complexRoute.viewHistory.pop())
            return true
        } else false
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

    override infix fun last(viewParent: Any?): Boolean {
        return if (hasHistory()) {
            if (viewParent != null) {
                for (route in history[getHistoryLast()].viewHistory) {
                    route.viewParent = viewParent
                }
            }
            val route = history.firstOrNull()?.viewHistory?.firstOrNull()

            return if (route != null) {
                routeTo(route)
                return true
            } else false
        } else false
    }

    override infix fun <B> next(route: Route<B>): Boolean {
        routeTo<B>(route)
        return true
    }

    override fun <B> next(route: Route<B>, replacementParams: B): Boolean {
        routeTo(route.copy(replacementParams))
        return true
    }

    override fun next(route: Route<Any>, replacementParams: Map<String, Any>): Boolean {
        routeTo(route.copy(replacementParams))
        return true
    }

    override infix fun replay(path: Path): Boolean {
        return if (hasHistory()) {
            for (i in 0 until history.size) {
                if (history[i].path == path && history[i].viewHistory.isNotEmpty()) {
                    routeTo(history[i].viewHistory.pop())
                    return true
                }
            }
            return false
        } else false
    }

    override fun <B> replayOrNext(route: Route<B>): Boolean {
        val canMove = route.path?.let { replay(route.path!!) } ?: false
        return if (!canMove) {
            next<B>(route)
        } else canMove
    }

    override infix fun back(block: () -> Unit): Boolean {
        log?.let {
            it.d(" <<--- Back")
            it.d(" History: ", history, this::getHistoryLast)
        }

        val result = when {
            history.isEmpty() -> false
            history[getHistoryLastWithoutPath()].viewHistory.isNotEmpty() ->
                internalBack(history[getHistoryLastWithoutPath()])
            else -> {
                history.removeAt(getHistoryLast())
                false
            }
        }

        if (!result) block()
        return result
    }

    override fun back(times: Int): Boolean {
        try {
            for (i in 0 until times) {
                if (!back({})) {
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

    override fun <B> back(route: Route<B>): Boolean {
        when {
            history.isEmpty() -> {
                log?.d("Is not possible to go back, history is empty")
                return false
            }
            history[getHistoryLast()].viewHistory.isEmpty() -> {
                history.removeAt(getHistoryLast())
                return back(route)
            }
            else -> {
                val complexRoute = history[getHistoryLast()]

                if (!complexRoute.viewHistory.isEmpty()) {
                    val size = complexRoute.viewHistory.size
                    for (i in size downTo 1) {
                        val prevRoute = complexRoute.viewHistory.pop()
                        if (route.clazz == prevRoute.clazz) {
                            this.routeTo(prevRoute)
                            return true
                        }
                    }
                } else if (complexRoute.route!!.clazz == route.clazz) {
                    history.removeAt(getHistoryLast())
                    this.routeTo(complexRoute.route)
                    return true
                } else {
                    log?.d("Is not possible to go back, there is no route like: " + route.clazz.name)
                    return false
                }
                history.removeAt(getHistoryLast())
                return back(route)
            }
        }
    }

    override infix fun back(path: Path): Boolean {
        log?.let {
            it.d(" <<--- Back")
            it.d(" History: ", history, this::getHistoryLast)
        }

        return when {
            history.isEmpty() -> false
            else -> {
                for (i in 0 until history.size) {
                    if (history[i].path == path) {
                        return internalBack(history[i])
                    }
                }
                false
            }
        }
    }

    @Suppress("UNCHECKED_CAST")
    override fun <B> current(): Route<B>? {
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
    override fun <T> payload(key: String): T? = current<Any>()?.internalParams?.get(key) as T?

    override fun clearHistory() = history.clear()

    override fun hasHistory() = !history.isEmpty()

    private fun createStartRoute(path: Path? = null) = history.add(ComplexRoute(path, null, ArrayDeque<Route<*>>()))

    private fun createIntermediateRoute(route: Route<*>) = history.add(ComplexRoute(route.path, route, ArrayDeque<Route<*>>()))

    private fun createViewRoute(route: Route<*>) {
        val complexRoute = history[getHistoryLast()]
        complexRoute.viewHistory.addFirst(route)
    }

    private fun createViewRouteOnPath(route: Route<*>) {
        for (i in 0 until history.size) {
            if (history[i].path == route.path) {
                history[i].viewHistory.addFirst(route)
                return
            }
        }
    }

    private fun getHistoryLast() = history.size - 1

    private fun getHistoryLastWithoutPath() = history.indexOfLast { it.path == null }

    private fun <B> areRoutesEqual(prev: Route<B>, next: Route<B>) =
            prev == next && (prev.bundle != null && (prev.bundle as B?)!! == next.bundle
                || prev.internalParams != null && Arrays.equals(
                    prev.internalParams!!.values.toTypedArray(), next.internalParams!!.values.toTypedArray()))
}