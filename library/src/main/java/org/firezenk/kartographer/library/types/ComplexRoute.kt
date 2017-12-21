package org.firezenk.kartographer.library.types

import java.util.*

class ComplexRoute internal constructor(internal val path: Path?, internal val route: Route<*>?,
                                        internal val viewHistory: ArrayDeque<Route<*>>) {
    override fun toString() = route.toString() + " viewHistory size: " + viewHistory.size
}