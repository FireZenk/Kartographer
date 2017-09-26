package org.firezenk.kartographer.library

/**
 * Project: Kartographer
 *
 * Created by Jorge Garrido Oval, aka firezenk on 20/09/17.
 * Copyright © Jorge Garrido Oval 2017
 */
interface IKartographer {

    /**
     * Enables debug mode for all the navigation session
     * @param debugMode true or false for all the session
     * *
     * @return the instance
     */
    fun debug(): Kartographer

    /**
     * Navigate to the last known route
     * @param context The Android's context (required for Android)
     * *
     * @param route The target route
     */
    fun last(context: Any, viewParent: Any?): Boolean

    /**
     * Navigate to the next route
     * @param context The Android's context (required for Android)
     * *
     * @param route The target route
     */
    fun <B> next(context: Any, route: Route<B>): Boolean

    /**
     * Go back to the directly previous route
     * @param context The Android's context (required for Android)
     * *
     * @return true if go back is possible, false if is the end of navigation history
     */
    fun back(context: Any): Boolean

    /**
     * Navigate back n times
     * @param context The Android's context (required for Android)
     * *
     * @param times The n times that we need to navigate backwards
     * *
     * @return true if go back n times is possible, false if is the end of navigation history
     */
    fun back(context: Any, times: Int): Boolean

    /**
     * Navigate through the navigation history until find the route
     * @param context The Android's context (required for Android)
     * *
     * @param route The route (params not needed) that we want to navigate back to
     * *
     * @return true if go back to this route is possible, false if it is not
     */
    fun <B> back(context: Any, route: Route<B>): Boolean

    /**
     * Clear navigation history
     */
    fun clearHistory()

    /**
     * Has history or not helper
     * @return true if the history is not empty
     */
    fun hasHistory(): Boolean
}