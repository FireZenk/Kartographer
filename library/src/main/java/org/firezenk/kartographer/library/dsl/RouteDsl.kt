package org.firezenk.kartographer.library.dsl

import org.firezenk.kartographer.annotations.RouteAnimation
import org.firezenk.kartographer.library.Route
import kotlin.reflect.KClass

@KartographerDsl
class RouteDsl<B> {

    class RouteBuilder<B> {

        var target: KClass<*>? = null
        var params: Map<String, Any>? = mapOf<String, Any>()
        var anchor: Any? = null
        var animation: RouteAnimation? = null
        var forResult: Int = -1

        fun build(): Route<B> = Route(target!!.java, params!!, anchor, animation, forResult)
    }
}