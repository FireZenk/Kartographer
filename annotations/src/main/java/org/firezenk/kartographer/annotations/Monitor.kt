package org.firezenk.kartographer.annotations

abstract class Monitor {

    abstract fun onContextChanged(block: (Any) -> Unit)

    abstract fun onContextCaptured(context: Any)
}