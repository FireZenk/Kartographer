package org.firezenk.kartographer.library.core

import org.firezenk.kartographer.library.Logger
import org.firezenk.kartographer.library.core.util.TargetRoute
import org.firezenk.kartographer.library.dsl.route
import org.firezenk.kartographer.library.dsl.routeActivity
import org.firezenk.kartographer.library.types.Path
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

    @Test fun `given a history with some paths, can go back to the previous path`() {
        val pathRouteOne = routeActivity<Any> {
            target = TargetRoute::class
            path = Path("PATH1")
            params = Any()
        }
        val routeOne = route {
            target = TargetRoute::class
            path = Path("PATH1")
            anchor = Any()
        }
        val pathRouteTwo = routeActivity<Any> {
            target = TargetRoute::class
            path = Path("PATH2")
            params = Any()
        }
        val routeTwo = route {
            target = TargetRoute::class
            path = Path("PATH2")
            anchor = Any()
        }
        forward.next(pathRouteOne)
        forward.next(routeOne)
        forward.next(pathRouteTwo)
        forward.next(routeTwo)

        assertEquals(3, core.history.size)
        assertEquals(1, core.history[pathRouteOne]!!.size)
        assertEquals(1, core.history[pathRouteTwo]!!.size)

        val result = backward.back {  }

        assertTrue(result)
        assertEquals(2, core.history.size)
        assertEquals(1, core.history[pathRouteOne]!!.size)
        assertFalse(core.pathExists(pathRouteTwo))
    }
}