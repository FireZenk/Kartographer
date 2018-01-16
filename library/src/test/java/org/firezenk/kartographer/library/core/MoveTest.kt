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

    @Test fun `given an empty history, add a new route on default path`() {
        val route = route {
            target = TargetRoute::class
            anchor = Any()
        }

        assertEquals(1, core.history.size)
        assertEquals(0, core.history[core.history.keys.first()]!!.size)

        move.routeTo(route)

        assertEquals(1, core.history.size)
        assertEquals(1, core.history[core.history.keys.first()]!!.size)
    }

    @Test fun `given an empty history, add a new route on a custom path`() {
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

    @Test fun `given an empty history, add a new path`() {
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

    @Test fun `given an empty history, add a new bundled route on a custom path`() {
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