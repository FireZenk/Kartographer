package org.firezenk.kartographer.library.dsl

import org.firezenk.kartographer.annotations.RouteAnimation
import org.firezenk.kartographer.library.core.Core.Companion.ROOT_NODE
import org.firezenk.kartographer.library.types.Path
import org.firezenk.kartographer.library.types.Route
import kotlin.reflect.KClass

@KartographerDsl
class RouteDsl {

    class RouteBuilder {

        var target: KClass<*>? = null
        var params: Map<String, Any>? = mapOf<String, Any>()
        var path: Path = ROOT_NODE
        var anchor: Any? = null
        var animation: RouteAnimation? = null
        var forResult: Int = -1

        fun build(): Route<Any> = Route(target!!.java, params!!, path, anchor, animation, forResult)
    }

    class RouteActivityBuilder<B> {

        var target: KClass<*>? = null
        var params: B? = null
        var path: Path = ROOT_NODE
        var animation: RouteAnimation? = null
        var forResult: Int = -1

        fun build(): Route<B> = Route<B>(target!!.java, params!!, path,null, animation, forResult)
    }
}