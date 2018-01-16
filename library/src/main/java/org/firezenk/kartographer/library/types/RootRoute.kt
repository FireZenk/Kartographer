package org.firezenk.kartographer.library.types

import org.firezenk.kartographer.annotations.RouteAnimation
import org.firezenk.kartographer.processor.interfaces.Routable
import java.util.*

internal class RootRoute : Routable {
    override fun route(context: Any, uuid: UUID, parameters: Any, viewParent: Any?, animation: RouteAnimation?) {}

    override fun path() = ""
}