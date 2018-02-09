package org.firezenk.kartographer.library.core

import org.firezenk.kartographer.library.Routable
import org.firezenk.kartographer.library.dsl.route
import org.firezenk.kartographer.library.exceptions.NotEnoughParametersException
import org.firezenk.kartographer.library.exceptions.ParameterNotFoundException
import org.firezenk.kartographer.library.types.*

/**
 * Project: Kartographer
 *
 * Created by Jorge Garrido Oval, aka firezenk on 21/12/17.
 * Copyright © Jorge Garrido Oval 2017
 */
class Move(private val core: Core) {

    fun routeTo(route: Route): Boolean = when(route) {
        is ViewRoute -> routeTo(route)
        is ContextRoute<*> -> routeTo(route)
        is ExternalRoute -> routeTo(route)
    }

    private fun routeTo(route: ViewRoute): Boolean {
        val prev: Route? = core.current()

        try {
            core.log?.let {
                it.d(" --->> Next")
                it.d(" Navigating to: ", route)
            }

            core.lastKnownPath = route.path

            if (core.pathIsValid(route, prev) && !core.pathExists(route)) {
                createPath(route)
                createViewRoute(route)
            } else if (!core.pathExists(route)) {
                createPath(route)
            } else {
                createViewRoute(route)
            }

            createView(route)
            return true
        } catch (e: Throwable) {
            handleError(e)
            return false
        }
    }

    private fun routeTo(route: ExternalRoute): Boolean {
        try {
            core.log?.let {
                it.d(" --->> Next")
                it.d(" Navigating to: ", route)
            }

            //TODO
            //(route.clazz as Routable<*>)
            //        .route(core.context, route.uuid, Any(), null, null)
            return true
        } catch (e: Throwable) {
            handleError(e)
            return false
        }
    }

    private fun <B> routeTo(route: ContextRoute<B>): Boolean {
        try {
            core.log?.let {
                it.d(" --->> Next")
                it.d(" Navigating to: ", route)
            }

            core.lastKnownPath = route.path

            createPathRoute(route)
            createView(route)
            return true
        } catch (e: Throwable) {
            handleError(e)
            return false
        }
    }

    private fun handleError(e: Throwable) = when(e) {
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


    private fun createView(route: ViewRoute) {
        (route.clazz as org.firezenk.kartographer.processor.interfaces.Routable)
                .route(core.context, route.uuid, route.params.values.toTypedArray(), route.viewParent, route.animation)
    }

    @Suppress("UNCHECKED_CAST")
    private fun <B> createView(route: ContextRoute<B>) {
        try {
            (route.clazz as Routable<B>)
                    .route(core.context, route.uuid, route.bundle as B, null, null)
        } catch (e: ClassCastException) {
            (route.clazz as org.firezenk.kartographer.processor.interfaces.Routable)
                    .route(core.context, route.uuid, route.bundle!!, null, null)
        }
    }

    private fun createPathRoute(route: ContextRoute<*>) = core.history.put(route, mutableListOf())

    private fun createViewRoute(route: Route) {
        val leaf: Route = core.history.keys.first { it.path == core.lastKnownPath }
        val branch: MutableList<Route>? = core.history[leaf]
        branch?.add(route)
    }

    private fun createPath(routeToAdd: ViewRoute) {
        val newBranch = route {
            target = RootRoute()
            path = routeToAdd.path
            anchor = Any()
        }

        val leaf: Route? = core.history.keys.firstOrNull { it.path == routeToAdd.path }

        leaf?.let {
            val branch: MutableList<Route>? = core.history[leaf]
            branch?.add(newBranch)
        } ?: core.history.put(newBranch, mutableListOf())
    }
}