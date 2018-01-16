package org.firezenk.kartographer.library.core.util

import org.firezenk.kartographer.annotations.RouteAnimation
import org.firezenk.kartographer.processor.interfaces.Routable
import java.util.*

class TargetRoute: Routable {
    override fun path() = "ROOT"

    override fun route(context: Any, uuid: UUID, parameters: Any,
                       viewParent: Any?, animation: RouteAnimation?) {}
}