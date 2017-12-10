package org.firezenk.kartographer.animations

import android.view.View
import org.firezenk.kartographer.annotations.RouteAnimation

class CrossFade : RouteAnimation {

    override fun animate(prev: Any, next: Any) {
        (prev as View).animate().alpha(0f).setDuration(500L).start()
        (next as View).animate().alpha(1f).setDuration(500L).start()
    }
}