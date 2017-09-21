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
class Kartographer : IKartographer {

    companion object {
        private var INSTANCE: Kartographer = Kartographer()

        fun get() : Kartographer = INSTANCE
    }

    private val history: ArrayList<ComplexRoute> = ArrayList()
    private var log: Logger? = null

    internal class ComplexRoute internal
    constructor(internal val route: Route<*>?, internal val viewHistory: ArrayDeque<Route<*>>) {

        override fun toString(): String {
            return route.toString() + " viewHistory size: " + viewHistory.size
        }
    }

    override fun debug(): Kartographer {
        log = Logger()
        return INSTANCE;
    }

    override fun <C, B> routeTo(context: C, route: Route<B>) {
        val prev: Route<B>? = if (history.isEmpty()) null else history.get(history.size - 1).viewHistory.peek() as Route<B>?
        try {
            if (prev == null || route.viewParent == null || !areRoutesEqual(prev, route)) {

                log?.d(" --->> Next")
                log?.d(" Navigating to: ", route)

                if (route.bundle != null) {
                    (route.clazz.newInstance() as Routable<C, B>)
                            .route(context, route.uuid, route.bundle as B, route.viewParent);
                } else {
                    (route.clazz.newInstance() as org.firezenk.kartographer.processor.interfaces.Routable<C>)
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
                    " times, the history length is " + history.size)
            log?.d(e.message!!)
            return false
        }
    }

    override fun <C, B> backTo(context: C, route: Route<B>): Boolean {
        if (history.isEmpty()) {
            log?.d("Is not possible to go back, history is empty")
            return false
        } else if (history[getHistoryLast()].viewHistory.isEmpty()) {
            history.removeAt(getHistoryLast())
            return backTo(context, route)
        } else {
            val complexRoute = history[getHistoryLast()]

            if (!complexRoute.viewHistory.isEmpty()) {
                val size = complexRoute.viewHistory.size
                for (i in size downTo 1) {
                    val prevRoute = complexRoute.viewHistory.pop()
                    if (route.clazz.equals(prevRoute.clazz)) {
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

    override fun clearHistory() {
        history.clear()
    }

    override fun hasHistory(): Boolean {
        return !history.isEmpty()
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