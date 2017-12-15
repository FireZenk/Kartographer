package org.firezenk.kartographer.annotations

/**
 * Project: Kartographer
 *
 * Created by Jorge Garrido Oval, aka firezenk on 20/09/17.
 * Copyright © Jorge Garrido Oval 2017
 */
@Retention(AnnotationRetention.SOURCE)
@Target(AnnotationTarget.CLASS)
annotation class RoutableView(
        /**
         * The path that holds the target route
         * @return the path string
         */
        val path: String = "",
        /**
         * Define the request code for the activity
         * @return the request code, -1 if not needed
         */
        val requestCode: Int = -1)