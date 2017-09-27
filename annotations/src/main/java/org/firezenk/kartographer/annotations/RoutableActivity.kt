package org.firezenk.kartographer.annotations

import kotlin.reflect.KClass

/**
 * Project: Kartographer
 *
 * Created by Jorge Garrido Oval, aka firezenk on 20/09/17.
 * Copyright © Jorge Garrido Oval 2017
 */
@Retention(AnnotationRetention.SOURCE)
@Target(AnnotationTarget.CLASS)
annotation class RoutableActivity(
        /**
         * The path that holds the target route
         * @return the path string
         */
        val path: String = "",
        /**
         * Possible "bundle" extras
         * @return array class types for the params
         */
        val params: Array<KClass<*>> = arrayOf(),
        /**
         * Define the request code for the activity
         * @return the request code, -1 if not needed
         */
        val requestCode: Int = -1)