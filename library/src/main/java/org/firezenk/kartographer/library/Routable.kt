package org.firezenk.kartographer.library

import org.firezenk.kartographer.annotations.RouteAnimation
import org.firezenk.kartographer.processor.exceptions.NotEnoughParametersException
import org.firezenk.kartographer.processor.exceptions.ParameterNotFoundException
import java.util.*

/**
 * Project: Kartographer
 *
 * Created by Jorge Garrido Oval, aka firezenk on 20/09/17.
 * Copyright © Jorge Garrido Oval 2017
 */
interface Routable<B> {

    @Throws(ParameterNotFoundException::class, NotEnoughParametersException::class)
    fun route(context: Any, uuid: UUID, parameters: B, viewParent: Any?, animation: RouteAnimation?)

    fun path(): String
}