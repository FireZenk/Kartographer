package org.firezenk.kartographer.library

import org.firezenk.kartographer.library.types.Path
import org.firezenk.kartographer.library.types.Route

/**
 * Project: Kartographer
 *
 * Created by Jorge Garrido Oval, aka firezenk on 20/09/17.
 * Copyright © Jorge Garrido Oval 2017
 */
interface IKartographer {

    /**
     * Enables debug mode for all the navigation session
     *
     * @param debugMode true or false for all the session
     *
     * @return the instance
     */
    fun debug(): Kartographer

    /**
     * Returns the current context in order to do some action inside a View
     *
     * @return the Context
     */
    fun <C> context(): C

    /**
     * Navigate to the last known route
     *
     * @param route The target route
     */
    infix fun last(viewParent: Any?): Boolean

    /**
     * Navigate to the next route
     *
     * @param route The target route
     */
    infix fun <B> next(route: Route<B>): Boolean

    /**
     * Navigate to the next route
     *
     * @param route The target route
     * @param params The parameters to replace the original ones
     */
    fun <B> next(route: Route<B>, replacementParams: B): Boolean

    /**
     * Navigate to the next route
     *
     * @param route The target route
     * @param params The parameters to replace the original ones
     */
    fun next(route: Route<Any>, replacementParams: Map<String, Any>): Boolean

    /**
     * Navigate to the last known route on the specified path
     *
     * @param path The path to search on
     */
    infix fun replay(path: Path): Boolean

    /**
     * Combines replay and next: first tries to replay if don't calls next
     *
     * @param route The target route
     */
    infix fun <B> replayOrNext(route: Route<B>): Boolean

    /**
     * Go back to the directly previous route
     *
     * @return true if go back is possible, false if is the end of navigation history
     */
    infix fun back(block: () -> Unit): Boolean

    /**
     * Navigate back n times
     *
     * @param times The n times that we need to navigate backwards
     *
     * @return true if go back n times is possible, false if is the end of navigation history
     */
    infix fun back(times: Int): Boolean

    /**
     * Navigate through the navigation history until find the route
     *
     * @param route The route (params not needed) that we want to navigate back to
     *
     * @return true if go back to this route is possible, false if it is not
     */
    infix fun <B> back(route: Route<B>): Boolean

    /**
     * Navigate back to the last known route within paths
     *
     * @return true if go back to this route is possible, false if it is not
     */
    infix fun backOnPath(block: () -> Unit): Boolean

    /**
     * Obtains the current route
     *
     * @return A route with specified type
     */
    fun <B> current(): Route<B>?

    /**
     * Obtains the value of the key inside payloads current route
     *
     * @return The payload with specified type
     */
    fun <T> payload(key: String): T?

    /**
     * Clear navigation history
     */
    fun clearHistory()

    /**
     * Has history or not helper
     *
     * @return true if the history is not empty
     */
    fun hasHistory(): Boolean
}