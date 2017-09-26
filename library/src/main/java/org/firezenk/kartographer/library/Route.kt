package org.firezenk.kartographer.library

import java.util.*

/**
 * Project: Kartographer
 *
 * Created by Jorge Garrido Oval, aka firezenk on 20/09/17.
 * Copyright © Jorge Garrido Oval 2017
 */
class Route<B> (val clazz: Class<*>, val params: Any, var viewParent: Any?, val forResult: Int = -1) {

    val uuid: UUID = UUID.randomUUID()
    var bundle: B? = null
    var internalParams: Array<Any>? = null

    init {
        this.getExtras(params)
    }

    override fun equals(other: Any?) = other is Route<*> && this.clazz == other.clazz

    @Suppress("UNCHECKED_CAST")
    private fun getExtras(params: Any) {
        try {
            this.internalParams = params as Array<Any>
        } catch (ex: ClassCastException) {
            this.bundle = params as B
        }
    }

    override fun toString() = "Route class name: ${clazz.simpleName} Has bundle? ${(bundle != null)} Has params? ${(internalParams != null)}"

    override fun hashCode(): Int{
        var result = clazz.hashCode()
        result = 31 * result + params.hashCode()
        result = 31 * result + (viewParent?.hashCode() ?: 0)
        result = 31 * result + forResult
        result = 31 * result + uuid.hashCode()
        result = 31 * result + (bundle?.hashCode() ?: 0)
        result = 31 * result + (internalParams?.let { Arrays.hashCode(it) } ?: 0)
        return result
    }
}