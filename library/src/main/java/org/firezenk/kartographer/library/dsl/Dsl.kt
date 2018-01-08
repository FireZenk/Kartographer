package org.firezenk.kartographer.library.dsl

import org.firezenk.kartographer.library.types.Route

@DslMarker
annotation class KartographerDsl

fun route(block: RouteDsl.RouteBuilder.() -> Unit): Route<Any> = RouteDsl.RouteBuilder().apply(block).build()

fun <B> routeActivity(block: RouteDsl.RouteActivityBuilder<B>.() -> Unit): Route<B> = RouteDsl.RouteActivityBuilder<B>().apply(block).build()