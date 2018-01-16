package org.firezenk.kartographer.library.core

import org.firezenk.kartographer.library.Logger
import org.firezenk.kartographer.library.core.util.TargetRoute
import org.firezenk.kartographer.library.dsl.route
import org.firezenk.kartographer.library.types.Path
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

/**
 * Created by Jorge Garrido Oval, aka firezenk on 14/01/18.
 * Project: Kartographer
 */
class ReplayTest {

    lateinit var core: Core
    lateinit var move: Move
    lateinit var forward: Forward
    lateinit var replay: Replay

    @Before fun setup() {
        core = Core(Any(), Logger())
        move = Move(core)
        forward = Forward(move)
        replay = Replay(core, move, forward)
    }

    @Test fun `given a history with one route on default path, the last one on path is repeated`() {
        val route = route {
            target = TargetRoute()
            path = Core.ROOT_NODE
            anchor = Any()
        }
        forward.next(route)

        assertEquals(1, core.history.size)
        assertEquals(1, core.history[core.history.keys.first()]!!.size)

        val result = replay.last(route.viewParent)

        assertTrue(result)
        assertEquals(1, core.history.size)
        assertEquals(2, core.history[core.history.keys.first()]!!.size)
    }

    @Test fun `given a history with one route on default path, the last one on path is replayed`() {
        val route = route {
            target = TargetRoute()
            path = Core.ROOT_NODE
            anchor = Any()
        }
        forward.next(route)

        assertEquals(1, core.history.size)
        assertEquals(1, core.history[core.history.keys.first()]!!.size)

        val result = replay.replay(Core.ROOT_NODE)

        assertTrue(result)
        assertEquals(1, core.history.size)
        assertEquals(1, core.history[core.history.keys.first()]!!.size)
    }

    @Test fun `given a history with one route on a custom path, return valid if can be replayed`() {
        val route = route {
            target = TargetRoute()
            path = Path("NOTE")
            anchor = Any()
        }
        forward.next(route)

        assertEquals(2, core.history.size)
        assertEquals(0, core.history[core.history.keys.first()]!!.size)
        assertEquals(1, core.history[core.history.keys.last()]!!.size)

        val result = replay.replayOrNext(route)

        assertTrue(result)
        assertEquals(2, core.history.size)
        assertEquals(0, core.history[core.history.keys.first()]!!.size)
        assertEquals(1, core.history[core.history.keys.last()]!!.size)
    }

    @Test fun `given a history with one route on a custom path, return valid if can be move to a new route`() {
        val route = route {
            target = TargetRoute()
            path = Path("NOTE")
            anchor = Any()
        }
        val customRoute = route {
            target = TargetRoute()
            path = Path("OTHER")
            anchor = Any()
        }
        forward.next(route)

        assertEquals(2, core.history.size)
        assertEquals(0, core.history[core.history.keys.first()]!!.size)
        assertEquals(1, core.history[core.history.keys.last()]!!.size)

        val result = replay.replayOrNext(customRoute)

        assertTrue(result)
        assertEquals(3, core.history.size)
        assertEquals(0, core.history[core.history.keys.first()]!!.size)
        assertEquals(1, core.history[core.history.keys.last()]!!.size)
    }
}