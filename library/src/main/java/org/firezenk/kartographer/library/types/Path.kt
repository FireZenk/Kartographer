package org.firezenk.kartographer.library.types

data class Path(private val namespace: String) {

    companion object {
        fun pathIsValid(route: Route<*>, prev: Route<*>?): Boolean {
            return route.path != null && route.path != prev?.path
        }

        fun pathExists(history: ArrayList<ComplexRoute>, path: Path?): Boolean {
            for (i in 0..(history.size -1)) {
                val it = history[i]
                if (it.path == path) {
                    return true
                }
                val viewHistory: List<Route<*>> = history[i].viewHistory.toList()
                for (j in 0 until viewHistory.size) {
                    if (viewHistory[j].path == path) {
                        return true
                    }
                }
            }
            return false
        }
    }

    override fun equals(other: Any?): Boolean = if (other != null) {
        val otherNamespace = (other as Path).namespace
        this.namespace == otherNamespace
    } else {
        false
    }

    override fun hashCode(): Int {
        return namespace.hashCode()
    }
}