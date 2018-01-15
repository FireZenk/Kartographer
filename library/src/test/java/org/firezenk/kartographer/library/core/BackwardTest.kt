package org.firezenk.kartographer.library.core

import org.firezenk.kartographer.library.Logger
import org.firezenk.kartographer.library.core.util.TargetRoute
import org.firezenk.kartographer.library.dsl.route
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

/**
 * Created by Jorge Garrido Oval, aka firezenk on 14/01/18.
 * Project: Kartographer
 */
class BackwardTest {

    lateinit var core: Core
    lateinit var move: Move
    lateinit var forward: Forward
    lateinit var replay: Replay
    lateinit var backward: Backward

    @Before fun setup() {
        core = Core(Any(), Logger())
        move = Move(core)
        forward = Forward(move)
        replay = Replay(core, move, forward)
        backward = Backward(core, move)
    }

    @Test fun `given a history with two route on default path, can go back to the previous one`() {
        val routeOne = route {
            target = TargetRoute::class
            path = Core.ROOT_NODE
            anchor = Any()
        }
        val routeTwo = route {
            target = TargetRoute::class
            path = Core.ROOT_NODE
            anchor = Any()
        }
        forward.next(routeOne)
        forward.next(routeTwo)

        assertEquals(1, core.history.size)
        assertEquals(2, core.history[core.history.keys.first()]!!.size)

        val result = backward.backOnPath {  }

        assertTrue(result)
        assertEquals(1, core.history.size)
        assertEquals(1, core.history[core.history.keys.first()]!!.size)
    }

    @Test fun `given a history with N routes on default path, can go back N - 1 times`() {
        val routeOne = route {
            target = TargetRoute::class
            path = Core.ROOT_NODE
            anchor = Any()
        }
        val routeTwo = route {
            target = TargetRoute::class
            path = Core.ROOT_NODE
            anchor = Any()
        }
        val routeThree = route {
            target = TargetRoute::class
            path = Core.ROOT_NODE
            anchor = Any()
        }
        forward.next(routeOne)
        forward.next(routeTwo)
        forward.next(routeThree)

        assertEquals(1, core.history.size)
        assertEquals(3, core.history[core.history.keys.first()]!!.size)

        val result = backward.back(2)

        assertTrue(result)
        assertEquals(1, core.history.size)
        assertEquals(1, core.history[core.history.keys.first()]!!.size)
        assertFalse(backward.back(1))
    }
}