package org.firezenk.kartographer.library.dsl

import org.firezenk.kartographer.library.Route
import kotlin.reflect.KClass

@KartographerDsl
class RouteDsl<B> {

    class RouteBuilder<B> {

        var clazz: KClass<*>? = null
        var params: Any = Any()
        var viewParent: Any? = null
        var forResult: Int = -1

        fun build(): Route<B> = Route(clazz!!.java, params, viewParent, forResult)
    }
}