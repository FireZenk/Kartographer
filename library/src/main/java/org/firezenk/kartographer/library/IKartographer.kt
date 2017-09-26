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
     * Navigate to the next route
     * @param context The Android's context (required for Android)
     * *
     * @param route The target route
     */
    fun <B> routeTo(context: Any, route: Route<B>)

    /**
     * Navigate to the last route available on history
     * @param context The Android's context (required for Android)
     * *
     * @param viewParent New view parent (for view recreations)
     */
    fun routeToLast(context: Any, viewParent: Any? = null)

    /**
     * Navigate to the last route on history, if available, or the new provided one
     * @param context The Android's context (required for Android)
     * *
     * @param route The new target route
     */
    fun <B> routeToLastOr(context: Any, route: Route<B>)

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
    fun backTimes(context: Any, times: Int): Boolean

    /**
     * Navigate through the navigation history until find the route
     * @param context The Android's context (required for Android)
     * *
     * @param route The route (params not needed) that we want to navigate back to
     * *
     * @return true if go back to this route is possible, false if it is not
     */
    fun <B> backTo(context: Any, route: Route<B>): Boolean

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