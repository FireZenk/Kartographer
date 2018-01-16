package org.firezenk.kartographer.library.dsl

import org.firezenk.kartographer.annotations.RouteAnimation
import org.firezenk.kartographer.library.core.Core.Companion.ROOT_NODE
import org.firezenk.kartographer.library.types.Path
import org.firezenk.kartographer.library.types.Route
import kotlin.reflect.KClass
import org.firezenk.kartographer.library.Routable as RoutableActivity
import org.firezenk.kartographer.processor.interfaces.Routable as RoutableView

@KartographerDsl
class RouteDsl {

    class RouteBuilder {

        var target: KClass<*>? = null
        var params: Map<String, Any>? = mapOf<String, Any>()
        var path: Path? = null
        var anchor: Any? = null
        var animation: RouteAnimation? = null
        var forResult: Int = -1

        private val calculatedPath: Path by lazy {
            if (target is RoutableView) {
                val castedTarget = target as RoutableView
                if (castedTarget.path() != "") {
                    Path(castedTarget.path())
                } else ROOT_NODE
            } else ROOT_NODE
        }

        fun build(): Route<Any> = Route(target!!.java, params!!, path ?: calculatedPath, anchor, animation, forResult)
    }

    class RouteActivityBuilder<B> {

        var target: KClass<*>? = null
        var params: B? = null
        var path: Path? = null
        var anchor: Any? = null
        var animation: RouteAnimation? = null
        var forResult: Int = -1

        private val calculatedPath: Path by lazy {
            if (target is RoutableActivity<*>) {
                val castedTarget = target as RoutableActivity<*>
                if (castedTarget.path() != "") {
                    Path(castedTarget.path())
                } else ROOT_NODE
            } else ROOT_NODE
        }

        fun build(): Route<B> = Route<B>(target!!.java, params!!, path ?: calculatedPath, anchor, animation, forResult)
    }
}