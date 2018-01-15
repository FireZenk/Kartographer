package org.firezenk.kartographer.library.core

import org.firezenk.kartographer.library.Logger
import org.firezenk.kartographer.library.core.util.TargetBundleRoute
import org.firezenk.kartographer.library.core.util.TargetRoute
import org.firezenk.kartographer.library.dsl.route
import org.firezenk.kartographer.library.dsl.routeActivity
import org.firezenk.kartographer.library.types.Path
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

/**
 * Created by Jorge Garrido Oval, aka firezenk on 14/01/18.
 * Project: Kartographer
 */
class MoveTest {

    lateinit var core: Core
    lateinit var move: Move

    @Before fun setup() {
        core = Core(Any(), Logger())
        move = Move(core)
    }

    @Test fun givenEmptyHistoryAddANewRouteOnDefaultPath() {
        val route = route {
            target = TargetRoute::class
            path = Core.ROOT_NODE
            anchor = Any()
        }

        assertEquals(1, core.history.size)
        assertEquals(0, core.history[core.history.keys.first()]!!.size)

        move.routeTo(route)

        assertEquals(1, core.history.size)
        assertEquals(1, core.history[core.history.keys.first()]!!.size)
    }

    @Test fun givenEmptyHistoryAddANewRouteOnANoDefaultPath() {
        val route = route {
            target = TargetRoute::class
            path = Path("CUSTOM_PATH")
            anchor = Any()
        }

        assertEquals(1, core.history.size)
        assertEquals(0, core.history[core.history.keys.first()]!!.size)

        move.routeTo(route)

        assertEquals(2, core.history.size)
        assertEquals(0, core.history[core.history.keys.first()]!!.size)
        assertEquals(1, core.history[core.history.keys.last()]!!.size)
    }

    @Test fun givenEmptyHistoryAddANewPath() {
        val route = route {
            target = TargetRoute::class
            path = Path("CUSTOM_PATH")
        }

        assertEquals(1, core.history.size)
        assertEquals(0, core.history[core.history.keys.first()]!!.size)

        move.routeTo(route)

        assertEquals(2, core.history.size)
        assertEquals(0, core.history[core.history.keys.first()]!!.size)
        assertEquals(0, core.history[core.history.keys.last()]!!.size)
    }

    @Test fun givenEmptyHistoryAddANewBundledRouteOnANoDefaultPath() {
        val route = routeActivity<Any> {
            target = TargetBundleRoute::class
            params = Any()
            path = Path("CUSTOM_PATH")
            anchor = Any()
        }

        assertEquals(1, core.history.size)
        assertEquals(0, core.history[core.history.keys.first()]!!.size)

        move.routeTo(route)

        assertEquals(2, core.history.size)
        assertEquals(0, core.history[core.history.keys.first()]!!.size)
        assertEquals(1, core.history[core.history.keys.last()]!!.size)
    }
}