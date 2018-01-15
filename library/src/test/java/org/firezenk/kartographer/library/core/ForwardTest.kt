package org.firezenk.kartographer.library.core

import org.firezenk.kartographer.library.Logger
import org.firezenk.kartographer.library.core.util.TargetRoute
import org.firezenk.kartographer.library.dsl.route
import org.firezenk.kartographer.library.types.Path
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

/**
 * Created by Jorge Garrido Oval, aka firezenk on 14/01/18.
 * Project: Kartographer
 */
class ForwardTest {

    lateinit var core: Core
    lateinit var move: Move
    lateinit var forward: Forward

    @Before fun setup() {
        core = Core(Any(), Logger())
        move = Move(core)
        forward = Forward(move)
    }

    @Test fun givenEmptyHistoryMoveNextOneTimeToDefaultPath() {
        val route = route {
            target = TargetRoute::class
            path = Core.ROOT_NODE
            anchor = Any()
        }

        assertEquals(1, core.history.size)
        assertEquals(0, core.history[core.history.keys.first()]!!.size)

        forward.next(route)

        assertEquals(1, core.history.size)
        assertEquals(1, core.history[core.history.keys.first()]!!.size)
    }

    @Test fun givenEmptyHistoryMoveNextOneTimeToANewPath() {
        val route = route {
            target = TargetRoute::class
            path = Path("CUSTOM_PATH")
            anchor = Any()
        }

        assertEquals(1, core.history.size)
        assertEquals(0, core.history[core.history.keys.first()]!!.size)

        forward.next(route)

        assertEquals(2, core.history.size)
        assertEquals(0, core.history[core.history.keys.first()]!!.size)
        assertEquals(1, core.history[core.history.keys.last()]!!.size)
    }
}