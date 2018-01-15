package org.firezenk.kartographer.library.core

import org.firezenk.kartographer.library.Routable
import org.firezenk.kartographer.library.dsl.route
import org.firezenk.kartographer.library.exceptions.NotEnoughParametersException
import org.firezenk.kartographer.library.exceptions.ParameterNotFoundException
import org.firezenk.kartographer.library.types.Route

/**
 * Project: Kartographer
 *
 * Created by Jorge Garrido Oval, aka firezenk on 21/12/17.
 * Copyright © Jorge Garrido Oval 2017
 */
class Move(private val core: Core) {

    @Suppress("UNCHECKED_CAST")
    fun <B> routeTo(route: Route<B>) {
        val prev: Route<B>? = core.current()

        try {
            core.log?.let {
                it.d(" --->> Next")
                it.d(" Navigating to: ", route)
            }

            core.lastKnownPath = route.path

            if (route.viewParent == null) {
                createPathRoute(route)
            } else {
                if (core.pathIsValid(route, prev) && !core.pathExists(route)) {
                    createPath(route)
                    createViewRoute(route)
                } else if (!core.pathExists(route)) {
                    createPath(route)
                } else {
                    createViewRoute(route)
                }
            }

            createView(route)
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

    private fun createPathRoute(route: Route<*>) = core.history.put(route, mutableListOf())

    private fun createViewRoute(route: Route<*>) {
        val leaf: Route<*> = core.history.keys.first { it.path == core.lastKnownPath }
        val branch: MutableList<Route<*>>? = core.history[leaf]
        branch?.add(route)
    }

    private fun createPath(routeToAdd: Route<*>) {
        val newBranch = route {
            target = Any::class
            path = routeToAdd.path
        }

        val leaf: Route<*>? = core.history.keys.firstOrNull { it.path == routeToAdd.path }

        leaf?.let {
            val branch: MutableList<Route<*>>? = core.history[leaf]
            branch?.add(newBranch)
        } ?: core.history.put(newBranch, mutableListOf())
    }
}