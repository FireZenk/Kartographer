package org.firezenk.kartographer.library.dsl

import org.firezenk.kartographer.annotations.RouteAnimation
import org.firezenk.kartographer.library.core.Core.Companion.ROOT_NODE
import org.firezenk.kartographer.library.types.ContextRoute
import org.firezenk.kartographer.library.types.ExternalRoute
import org.firezenk.kartographer.library.types.Path
import org.firezenk.kartographer.library.types.ViewRoute
import org.firezenk.kartographer.processor.interfaces.Routable

/**
 * Project: Kartographer
 *
 * Created by Jorge Garrido Oval, aka firezenk on 01/12/17.
 * Copyright © Jorge Garrido Oval 2017
 */
@KartographerDsl
class RouteDsl {

    class RouteBuilder {

        var target: Any? = null
        var params: Map<String, Any>? = mapOf<String, Any>()
        var path: Path? = null
        var anchor: Any? = null
        var animation: RouteAnimation? = null
        var forResult: Int = -1

        private val calculatedPath: Path by lazy {
            if (target is Routable) {
                val castedTarget = target as Routable
                if (castedTarget.path() != "") {
                    Path(castedTarget.path())
                } else ROOT_NODE
            } else ROOT_NODE
        }

        fun build(): ViewRoute = ViewRoute(target!!, params ?: mapOf(), path ?: calculatedPath, anchor!!,
                animation, forResult)
    }

    class RouteActivityBuilder<B> {

        var target: Any? = null
        var params: B? = null
        var path: Path? = null
        var forResult: Int = -1

        private val calculatedPath: Path by lazy {
            if (target is Routable) {
                val castedTarget = target as Routable
                if (castedTarget.path() != "") {
                    Path(castedTarget.path())
                } else ROOT_NODE
            } else ROOT_NODE
        }

        fun build(): ContextRoute<B> = ContextRoute<B>(target!!, params!!, path ?: calculatedPath, forResult)
    }

    class RouteExternalBuilder {

        var target: Any? = null

        fun build(): ExternalRoute = ExternalRoute(target!!)
    }
}