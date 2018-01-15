package org.firezenk.kartographer.library.core

import org.firezenk.kartographer.library.Logger
import org.firezenk.kartographer.library.core.util.TargetRoute
import org.firezenk.kartographer.library.dsl.route
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

/**
 * Created by Jorge Garrido Oval, aka firezenk on 14/01/18.
 * Project: Kartographer
 */
class CoreTest {

    lateinit var core: Core
    lateinit var move: Move

    @Before fun setup() {
        core = Core(Any(), Logger())
        move = Move(core)
    }

    @Test fun `given a history with one route on default path, the current route is correct`() {
        val route = route {
            target = TargetRoute::class
            path = Core.ROOT_NODE
            anchor = Any()
        }
        move.routeTo(route)

        val currentRoute = core.current<Any>()

        assertEquals(route, currentRoute)
    }

    @Test fun `given a history with one route on default path, the current route payload is correct`() {
        val route = route {
            target = TargetRoute::class
            path = Core.ROOT_NODE
            params = mapOf("param1" to 1, "param2" to "hi!")
            anchor = Any()
        }
        move.routeTo(route)

        val currentParam1 = core.payload<Int>("param1")
        val currentParam2 = core.payload<String>("param2")

        assertEquals(currentParam1, 1)
        assertEquals(currentParam2, "hi!")
    }
}