package org.firezenk.kartographer.library.types

import org.firezenk.kartographer.annotations.RouteAnimation
import java.util.*

/**
 * Project: Kartographer
 *
 * Created by Jorge Garrido Oval, aka firezenk on 20/09/17.
 * Copyright © Jorge Garrido Oval 2017
 */
sealed class Route(open val clazz: Any, open var path: Path, open val forResult: Int = -1) {
    val uuid: UUID = UUID.randomUUID()
}

class ViewRoute(override val clazz: Any, val params: Map<String, Any>, override var path: Path, var viewParent: Any,
                val animation: RouteAnimation?, override val forResult: Int = -1) : Route(clazz, path, forResult) {

    companion object {
        fun areRoutesEqual(prev: ViewRoute, next: ViewRoute) =
                prev == next && Arrays.equals(prev.params.values.toTypedArray(), next.params.values.toTypedArray())
    }

    override fun equals(other: Any?) = other is ViewRoute && clazz == other.clazz

    override fun toString() = "Route name: ${clazz.javaClass.simpleName}, on path: $path, params: (${params.keys})"

    override fun hashCode(): Int {
        var result = clazz.hashCode()
        result = 31 * result + params.hashCode()
        result = 31 * result + viewParent.hashCode()
        result = 31 * result + forResult
        result = 31 * result + uuid.hashCode()
        return result
    }

    fun copy(replacementParams: Map<String, Any>)
            = ViewRoute(clazz, replacementParams, path, viewParent, animation, forResult)

}

class ContextRoute<out B>(override val clazz: Any, val bundle: B, override var path: Path, override val forResult: Int = -1)
    : Route(clazz, path, forResult) {

    companion object {
        fun <B> areRoutesEqual(prev: ContextRoute<B>, next: ContextRoute<B>) =
                prev == next && (prev.bundle != null && (prev.bundle as B?)!! == next.bundle)
    }

    override fun equals(other: Any?) = other is ContextRoute<*> && clazz == other.clazz

    override fun toString() = "Route name: ${clazz.javaClass.simpleName}, on path: $path, has bundle? ${(bundle != null)}"

    override fun hashCode(): Int {
        var result = clazz.hashCode()
        result = 31 * result + forResult
        result = 31 * result + uuid.hashCode()
        result = 31 * result + (bundle?.hashCode() ?: 0)
        return result
    }

    fun <B> copy(replacementParams: B) = ContextRoute<B>(clazz, replacementParams, path, forResult)
}

class ExternalRoute(override val clazz: Any, val params: Map<String, Any>, override var path: Path = Path(""),
                    override val forResult: Int = -1) : Route(clazz, path, forResult) {

    fun copy(replacementParams: Map<String, Any>) = ExternalRoute(clazz, replacementParams, path)
}