package org.firezenk.kartographer.library.dsl

import org.firezenk.kartographer.library.Route

@DslMarker
annotation class KartographerDsl

fun route(block: RouteDsl.RouteBuilder.() -> Unit): Route<Any> = RouteDsl.RouteBuilder().apply(block).build()