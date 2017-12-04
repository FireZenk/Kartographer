package org.firezenk.kartographer.library.dsl

import org.firezenk.kartographer.library.Route

@DslMarker
annotation class KartographerDsl

fun <B> route(block: RouteDsl.RouteBuilder<B>.() -> Unit): Route<B> = RouteDsl.RouteBuilder<B>().apply(block).build()