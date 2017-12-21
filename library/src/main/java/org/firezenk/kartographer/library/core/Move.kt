package org.firezenk.kartographer.library.core

import org.firezenk.kartographer.library.Routable
import org.firezenk.kartographer.library.exceptions.NotEnoughParametersException
import org.firezenk.kartographer.library.exceptions.ParameterNotFoundException
import org.firezenk.kartographer.library.types.ComplexRoute
import org.firezenk.kartographer.library.types.Path
import org.firezenk.kartographer.library.types.Path.Companion.pathExists
import org.firezenk.kartographer.library.types.Path.Companion.pathIsValid
import org.firezenk.kartographer.library.types.Route
import org.firezenk.kartographer.library.types.Route.Companion.areRoutesEqual
import java.util.*

/**
 * Project: Kartographer
 *
 * Created by Jorge Garrido Oval, aka firezenk on 21/12/17.
 * Copyright © Jorge Garrido Oval 2017
 */
class Move(private val core: Core) {

    @Suppress("UNCHECKED_CAST")
    fun <B> routeTo(route: Route<B>) {
        val prev: Route<B>? = if (core.history.isEmpty()) null else core.history[core.history.size - 1].viewHistory.peek() as Route<B>?
        try {
            if (prev == null || route.viewParent == null || !areRoutesEqual(prev, route)) {
                core.log?.let {
                    it.d(" --->> Next")
                    it.d(" Navigating to: ", route)
                }

                core.lastKnownPath = route.path

                if (core.history.size == 0) {
                    createStartRoute()
                }

                if (route.viewParent == null) {
                    createIntermediateRoute(route)
                } else {
                    if (pathIsValid(route, prev) && !pathExists(core.history, route.path)) {
                        createStartRoute(route.path)
                        createViewRoute(route)
                    } else if (pathExists(core.history, route.path)) {
                        createViewRouteOnPath(route)
                    } else {
                        createViewRoute(route)
                    }
                }

                createView(route)
            }
        } catch (e: Throwable) {
            when(e) {
                is ClassCastException -> core.log?.d(" Params has to be instance of Object[] or Android's Bundle ", e)
                is ParameterNotFoundException,
                is NotEnoughParametersException,
                is InstantiationException,
                is IllegalAccessException,
                is org.firezenk.kartographer.processor.exceptions.NotEnoughParametersException,
                is org.firezenk.kartographer.processor.exceptions.ParameterNotFoundException -> {
                    core.log?.d(" Navigation error: ", e.fillInStackTrace())
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
                        .route(core.context, route.uuid, route.bundle as B, route.viewParent, route.animation)
            } catch (e: ClassCastException) {
                (route.clazz.newInstance() as org.firezenk.kartographer.processor.interfaces.Routable)
                        .route(core.context, route.uuid, route.bundle!!, route.viewParent, route.animation)
            }
        } else {
            (route.clazz.newInstance() as org.firezenk.kartographer.processor.interfaces.Routable)
                    .route(core.context, route.uuid, (route.params as Map<String, Any>).values.toTypedArray(), route.viewParent, route.animation)
        }
    }

    private fun createStartRoute(path: Path? = null) = core.history.add(ComplexRoute(path, null, ArrayDeque<Route<*>>()))

    private fun createIntermediateRoute(route: Route<*>) = core.history.add(ComplexRoute(route.path, route, ArrayDeque<Route<*>>()))

    private fun createViewRoute(route: Route<*>) {
        val complexRoute = core.history.last()
        complexRoute.viewHistory.addFirst(route)
    }

    private fun createViewRouteOnPath(route: Route<*>) {
        for (i in 0 until core.history.size) {
            if (core.history[i].path == route.path) {
                core.history[i].viewHistory.addFirst(route)
                return
            }
        }
    }
}