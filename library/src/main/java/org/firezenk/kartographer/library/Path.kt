package org.firezenk.kartographer.library

data class Path(private val namespace: String) {

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