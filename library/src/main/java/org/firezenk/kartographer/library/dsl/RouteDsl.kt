package org.firezenk.kartographer.library.dsl

import org.firezenk.kartographer.library.Route
import kotlin.reflect.KClass

@KartographerDsl
class RouteDsl<B> {

    class RouteBuilder<B> {

        var target: KClass<*>? = null
        var params: Any = Any()
        var anchor: Any? = null
        var forResult: Int = -1

        fun build(): Route<B> = Route(target!!.java, params, anchor, forResult)
    }
}