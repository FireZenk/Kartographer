package org.firezenk.kartographer.annotations

interface RouteAnimation {

    fun prepare(prev: Any, next: Any)

    fun animate(prev: Any, next: Any, block: () -> Unit)
}