package org.firezenk.kartographer.library.dsl

import org.firezenk.kartographer.library.types.ContextRoute
import org.firezenk.kartographer.library.types.ExternalRoute
import org.firezenk.kartographer.library.types.ViewRoute

@DslMarker
annotation class KartographerDsl

fun route(block: RouteDsl.RouteBuilder.() -> Unit): ViewRoute = RouteDsl.RouteBuilder().apply(block).build()

fun routeExternal(block: RouteDsl.RouteExternalBuilder.() -> Unit): ExternalRoute = RouteDsl.RouteExternalBuilder().apply(block).build()

fun <B> routeActivity(block: RouteDsl.RouteActivityBuilder<B>.() -> Unit): ContextRoute<B> = RouteDsl.RouteActivityBuilder<B>().apply(block).build()