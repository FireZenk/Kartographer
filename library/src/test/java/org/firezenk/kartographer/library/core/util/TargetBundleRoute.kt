package org.firezenk.kartographer.library.core.util

import org.firezenk.kartographer.annotations.RouteAnimation
import org.firezenk.kartographer.library.ContextRoutable
import org.firezenk.kartographer.library.core.Core
import java.util.*

class TargetBundleRoute : ContextRoutable<Any> {
    override fun path() = Core.ROOT_NODE.toString()

    override fun route(context: Any, uuid: UUID, parameters: Any, viewParent: Any?, animation: RouteAnimation?) {}
}