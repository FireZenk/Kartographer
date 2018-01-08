package org.firezenk.kartographer.library

import org.firezenk.kartographer.annotations.Monitor
import org.firezenk.kartographer.library.core.*
import org.firezenk.kartographer.library.types.Path
import org.firezenk.kartographer.library.types.Route

/**
 * Project: Kartographer
 *
 * Created by Jorge Garrido Oval, aka firezenk on 20/09/17.
 * Copyright © Jorge Garrido Oval 2017
 */
class Kartographer(context: Any, monitor: Monitor) : IKartographer {

    private val core: Core = Core(context)
    private var move: Move
    private var forward: Forward
    private var backward: Backward
    private var replay: Replay

    init {
        monitor.onContextChanged { core.context = it }
        move = Move(core)
        forward = Forward(move)
        replay = Replay(core, move, forward)
        backward = Backward(core, move)
    }

    override fun debug(): Kartographer {
        core.log = Logger()
        return this
    }

    override fun last(viewParent: Any?) = replay.last(viewParent)

    override fun <B> next(route: Route<B>) = forward.next(route)

    override fun <B> next(route: Route<B>, replacementParams: B) = forward.next(route, replacementParams)

    override fun next(route: Route<Any>, replacementParams: Map<String, Any>) = forward.next(route, replacementParams)

    override fun replay(path: Path) = replay.replay(path)

    override fun <B> replayOrNext(route: Route<B>) = replay.replayOrNext(route)

    override fun back(block: () -> Unit) = backward.back(block)

    override fun back(times: Int) = backward.back(times)

    override fun <B> back(route: Route<B>) = TODO("backward.back(route)")

    override fun backOnPath(block: () -> Unit) = backward.backOnPath(block)

    override fun <B> current(): Route<B>? = core.current()

    override fun <T> payload(key: String): T? = core.payload(key)

    override fun clearHistory() = core.clearHistory()

    override fun hasHistory() = core.hasHistory()
}