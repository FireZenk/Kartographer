package org.firezenk.kartographer.processor.interfaces

import org.firezenk.kartographer.processor.exceptions.NotEnoughParametersException
import org.firezenk.kartographer.processor.exceptions.ParameterNotFoundException
import java.util.*

/**
 * Project: Kartographer
 *
 * Created by Jorge Garrido Oval, aka firezenk on 20/09/17.
 * Copyright © Jorge Garrido Oval 2017
 */
interface Routable {

    @Throws(ParameterNotFoundException::class, NotEnoughParametersException::class)
    fun route(context: Any, uuid: UUID, parameters: Array<Any>, viewParent: Any?)

    fun path(): String
}