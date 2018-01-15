package org.firezenk.kartographer.library.core.util

import org.firezenk.kartographer.annotations.RouteAnimation
import org.firezenk.kartographer.library.core.Core
import org.firezenk.kartographer.processor.interfaces.Routable
import java.util.*

class TargetRoute: Routable {
    override fun path() = Core.ROOT_NODE.toString()

    override fun route(context: Any, uuid: UUID, parameters: Any,
                       viewParent: Any?, animation: RouteAnimation?) {}
}