package org.firezenk.kartographer.library.types

import java.util.*

data class ComplexRoute internal constructor(internal val path: Path, internal val viewHistory: Array<Route<*>>) {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as ComplexRoute

        if (path != other.path) return false
        if (!Arrays.equals(viewHistory, other.viewHistory)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = path.hashCode()
        result = 31 * result + Arrays.hashCode(viewHistory)
        return result
    }
}