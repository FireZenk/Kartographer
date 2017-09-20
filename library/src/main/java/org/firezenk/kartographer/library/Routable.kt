package org.firezenk.kartographer.library

import org.firezenk.kartographer.processor.exceptions.NotEnoughParametersException
import org.firezenk.kartographer.processor.exceptions.ParameterNotFoundException
import java.util.*

/**
 * Project: Kartographer
 *
 * Created by Jorge Garrido Oval, aka firezenk on 20/09/17.
 * Copyright © Jorge Garrido Oval 2017
 */
interface Routable<C, B> {

    @Throws(ParameterNotFoundException::class, NotEnoughParametersException::class)
    fun route(context: C, uuid: UUID, parameters: B, viewParent: Any?)
}