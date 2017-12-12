package org.firezenk.kartographer.annotations

abstract class RouteAnimation(val animTime: Long = 200) {

    abstract fun prepare(prev: Any, next: Any)

    abstract fun animate(prev: Any, next: Any, block: () -> Unit)
}