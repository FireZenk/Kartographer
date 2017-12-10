package org.firezenk.kartographer.animations

import android.animation.Animator
import android.view.View
import org.firezenk.kartographer.annotations.RouteAnimation

class CrossFade : RouteAnimation {

    private val animTime = 200L

    override fun prepare(prev: Any, next: Any) {
        (next as View).alpha = 0f
    }

    override fun animate(prev: Any, next: Any, block: () -> Unit) {
        (prev as View).animate().alpha(0f).setDuration(animTime).start()
        (next as View).animate().alpha(1f).setDuration(animTime).setListener(object : Animator.AnimatorListener {
            override fun onAnimationRepeat(p0: Animator?) {}

            override fun onAnimationEnd(p0: Animator?) {
                block()
            }

            override fun onAnimationCancel(p0: Animator?) {}

            override fun onAnimationStart(p0: Animator?) {}
        }).start()
    }
}