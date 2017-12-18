package org.firezenk.kartographer.library.dsl

import org.firezenk.kartographer.annotations.RouteAnimation
import org.firezenk.kartographer.library.Route
import kotlin.reflect.KClass

@KartographerDsl
class RouteDsl {

    class RouteBuilder {

        var target: KClass<*>? = null
        var params: Map<String, Any>? = mapOf<String, Any>()
        var anchor: Any? = null
        var animation: RouteAnimation? = null
        var forResult: Int = -1

        fun build(): Route<Any> = Route(target!!.java, params!!, anchor, animation, forResult)
    }

    class RouteActivityBuilder<B> {

        var target: KClass<*>? = null
        var params: B? = null
        var animation: RouteAnimation? = null
        var forResult: Int = -1

        fun build(): Route<B> = Route<B>(target!!.java, params!!, null, animation, forResult)
    }
}